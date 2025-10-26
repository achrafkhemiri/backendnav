package com.example.navire.services;

import com.example.navire.dto.ProjetDTO;
import com.example.navire.exception.ProjetNotFoundException;
import com.example.navire.mapper.ProjetMapper;
import com.example.navire.model.Client;
import com.example.navire.model.Projet;
import com.example.navire.model.Depot;
import com.example.navire.model.Societe;
import com.example.navire.model.ProjetClient;
import com.example.navire.model.ProjetDepot;
import com.example.navire.repository.ProjetRepository;
import com.example.navire.repository.SocieteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjetService {
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private ProjetMapper projetMapper;
    @Autowired
    private com.example.navire.repository.ClientRepository clientRepository;
    @Autowired
    private com.example.navire.repository.DepotRepository depotRepository;
    @Autowired
    private SocieteRepository societeRepository;
    @Autowired
    private QuantiteService quantiteService;

    public List<ProjetDTO> getAllProjets() {
        return projetRepository.findAll().stream()
                .map(projetMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ProjetDTO getProjetById(Long id) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ProjetNotFoundException(id));
        return projetMapper.toDTO(projet);
    }

    @Transactional
    public ProjetDTO createProjet(ProjetDTO dto) {
        Projet projet = projetMapper.toEntity(dto);
        projet.setEtat(dto.getEtat());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());
        projet.setActive(dto.getActive() != null ? dto.getActive() : true);
        
        // Associer les sociétés si fournies
        Set<Societe> societes = new HashSet<>();
        // 1) Nouvelles sociétés en tant qu'objets complets
        if (dto.getSocietes() != null && !dto.getSocietes().isEmpty()) {
            for (com.example.navire.dto.SocieteDTO sDto : dto.getSocietes()) {
                Societe s = null;
                if (sDto.getId() != null) {
                    s = societeRepository.findById(sDto.getId()).orElse(null);
                }
                if (s == null && sDto.getNom() != null) {
                    s = societeRepository.findByNom(sDto.getNom()).orElse(null);
                }
                if (s == null) {
                    s = new Societe();
                }
                // Mettre à jour/initialiser les champs connus
                s.setNom(sDto.getNom());
                s.setAdresse(sDto.getAdresse());
                s.setRcs(sDto.getRcs());
                s.setContact(sDto.getContact());
                s.setTva(sDto.getTva());
                s = societeRepository.save(s);
                societes.add(s);
            }
        }
        // 2) Compatibilité: association via noms
        if (dto.getSocieteNoms() != null && !dto.getSocieteNoms().isEmpty()) {
            for (String societeNom : dto.getSocieteNoms()) {
                Societe societe = societeRepository.findByNom(societeNom)
                        .orElseGet(() -> {
                            Societe nouvelleSociete = new Societe();
                            nouvelleSociete.setNom(societeNom);
                            return societeRepository.save(nouvelleSociete);
                        });
                societes.add(societe);
            }
        }
        if (!societes.isEmpty()) {
            projet.setSocietes(societes);
        }
        
        return projetMapper.toDTO(projetRepository.save(projet));
    }

    @Transactional
    public ProjetDTO updateProjet(Long id, ProjetDTO dto) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new ProjetNotFoundException(id));
        
        projet.setNomProduit(dto.getNomProduit());
        projet.setQuantiteTotale(dto.getQuantiteTotale());
        projet.setNomNavire(dto.getNomNavire());
        projet.setPaysNavire(dto.getPaysNavire());
        projet.setEtat(dto.getEtat());
        projet.setDateDebut(dto.getDateDebut());
        projet.setDateFin(dto.getDateFin());
        projet.setActive(dto.getActive() != null ? dto.getActive() : projet.getActive());
        
        // Mettre à jour les sociétés
        if (dto.getSocietes() != null || dto.getSocieteNoms() != null) {
            Set<Societe> societesMaj = new HashSet<>();
            if (dto.getSocietes() != null) {
                for (com.example.navire.dto.SocieteDTO sDto : dto.getSocietes()) {
                    Societe s = null;
                    if (sDto.getId() != null) {
                        s = societeRepository.findById(sDto.getId()).orElse(null);
                    }
                    if (s == null && sDto.getNom() != null) {
                        s = societeRepository.findByNom(sDto.getNom()).orElse(null);
                    }
                    if (s == null) {
                        s = new Societe();
                    }
                    s.setNom(sDto.getNom());
                    s.setAdresse(sDto.getAdresse());
                    s.setRcs(sDto.getRcs());
                    s.setContact(sDto.getContact());
                    s.setTva(sDto.getTva());
                    s = societeRepository.save(s);
                    societesMaj.add(s);
                }
            }
            if (dto.getSocieteNoms() != null) {
                for (String societeNom : dto.getSocieteNoms()) {
                    Societe s = societeRepository.findByNom(societeNom)
                            .orElseGet(() -> {
                                Societe nouvelleSociete = new Societe();
                                nouvelleSociete.setNom(societeNom);
                                return societeRepository.save(nouvelleSociete);
                            });
                    societesMaj.add(s);
                }
            }
            projet.setSocietes(societesMaj);
        }
        
        return projetMapper.toDTO(projetRepository.save(projet));
    }

    @Transactional
    public void deleteProjet(Long id) {
        if (!projetRepository.existsById(id)) {
            throw new ProjetNotFoundException(id);
        }
        projetRepository.deleteById(id);
    }

    @Transactional
    public void addClientToProjet(Long projetId, Long clientId) {
        addClientToProjet(projetId, clientId, 0.0);
    }

    @Transactional
    public void addClientToProjet(Long projetId, Long clientId, Double quantiteAutorisee) {
        // Valider la quantité avant d'ajouter le client
        double quantite = quantiteAutorisee != null ? quantiteAutorisee : 0.0;
        QuantiteService.ValidationResult validation = quantiteService.validerAjoutClient(projetId, quantite);
        
        if (!validation.isValide()) {
            throw new IllegalArgumentException(validation.getMessage());
        }
        
        Projet projet = projetRepository.findById(projetId).orElseThrow(() -> new ProjetNotFoundException(projetId));
        Client client = clientRepository.findById(clientId).orElseThrow();
        ProjetClient pc = new ProjetClient();
        pc.setProjet(projet);
        pc.setClient(client);
        pc.setQuantiteAutorisee(quantite);
        projet.getProjetClients().add(pc);
        projetRepository.save(projet);
    }

    @Transactional
    public void addDepotToProjet(Long projetId, Long depotId, Double quantiteAutorisee) {
        Projet projet = projetRepository.findById(projetId).orElseThrow(() -> new ProjetNotFoundException(projetId));
        Depot depot = depotRepository.findById(depotId).orElseThrow();
        
        ProjetDepot projetDepot = new ProjetDepot();
        projetDepot.setProjet(projet);
        projetDepot.setDepot(depot);
        projetDepot.setQuantiteAutorisee(quantiteAutorisee != null ? quantiteAutorisee : 0.0);
        
        if (projet.getProjetDepots() == null) {
            projet.setProjetDepots(new HashSet<>());
        }
        projet.getProjetDepots().add(projetDepot);
        projetRepository.save(projet);
    }

    /**
     * Récupère tous les dépôts associés à un projet spécifique
     */
    public List<Depot> getDepotsByProjetId(Long projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ProjetNotFoundException(projetId));
        return projet.getProjetDepots().stream()
                .map(ProjetDepot::getDepot)
                .collect(Collectors.toList());
    }

    /**
     * Ajoute une société à un projet
     */
    @Transactional
    public void addSocieteToProjet(Long projetId, Long societeId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ProjetNotFoundException(projetId));
        Societe societe = societeRepository.findById(societeId)
                .orElseThrow(() -> new RuntimeException("Société non trouvée avec l'id: " + societeId));
        
        if (projet.getSocietes() == null) {
            projet.setSocietes(new HashSet<>());
        }
        projet.getSocietes().add(societe);
        projetRepository.save(projet);
    }

    /**
     * Retire une société d'un projet
     */
    @Transactional
    public void removeSocieteFromProjet(Long projetId, Long societeId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ProjetNotFoundException(projetId));
        Societe societe = societeRepository.findById(societeId)
                .orElseThrow(() -> new RuntimeException("Société non trouvée avec l'id: " + societeId));
        
        if (projet.getSocietes() != null) {
            projet.getSocietes().remove(societe);
            projetRepository.save(projet);
        }
    }

    /**
     * Récupère toutes les sociétés associées à un projet
     */
    public List<Societe> getSocietesByProjetId(Long projetId) {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new ProjetNotFoundException(projetId));
        return projet.getSocietes() != null 
                ? projet.getSocietes().stream().collect(Collectors.toList())
                : List.of();
    }
}
