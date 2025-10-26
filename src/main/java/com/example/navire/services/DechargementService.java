package com.example.navire.services;

import com.example.navire.dto.DechargementDTO;
import com.example.navire.exception.*;
import com.example.navire.mapper.DechargementMapper;
import com.example.navire.model.*;
import com.example.navire.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DechargementService {

    private final DechargementRepository dechargementRepository;
    private final DechargementMapper dechargementMapper;
    private final ChargementRepository chargementRepository;
    private final ClientRepository clientRepository;
    private final DepotRepository depotRepository;
    private final VoyageRepository voyageRepository;

    @Transactional(readOnly = true)
    public List<DechargementDTO> getAllDechargements() {
        return dechargementRepository.findAll()
                .stream()
                .map(dechargementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DechargementDTO getDechargementById(Long id) {
        Dechargement dechargement = dechargementRepository.findById(id)
                .orElseThrow(() -> new DechargementNotFoundException(id));
        return dechargementMapper.toDTO(dechargement);
    }

    @Transactional
    public DechargementDTO createDechargement(DechargementDTO dechargementDTO) {
        // Valider que soit le client, soit le dépôt est fourni (au moins un des deux)
        if (dechargementDTO.getClientId() == null && dechargementDTO.getDepotId() == null) {
            throw new IllegalArgumentException("Au moins un client ou un dépôt doit être spécifié");
        }

        // Valider les poids: obligatoires, entiers, et brut > tar
        if (dechargementDTO.getPoidCamionVide() == null || dechargementDTO.getPoidComplet() == null) {
            throw new IllegalArgumentException("Les poids camion tar et poids brut sont obligatoires");
        }
        Double poidVideDto = dechargementDTO.getPoidCamionVide();
        Double poidBrutDto = dechargementDTO.getPoidComplet();
        boolean videIsInt = Math.floor(poidVideDto) == poidVideDto;
        boolean brutIsInt = Math.floor(poidBrutDto) == poidBrutDto;
        if (!videIsInt || !brutIsInt) {
            throw new IllegalArgumentException("Les poids doivent être des entiers (sans décimales)");
        }
        if (poidBrutDto <= poidVideDto) {
            throw new IllegalArgumentException("Le poids brut doit être strictement supérieur au poids tar");
        }
        
        // Valider l'existence des entités liées
        Chargement chargement = chargementRepository.findById(dechargementDTO.getChargementId())
                .orElseThrow(() -> new ChargementNotFoundException(dechargementDTO.getChargementId()));
        
        // Client est optionnel
        Client client = null;
        if (dechargementDTO.getClientId() != null) {
            client = clientRepository.findById(dechargementDTO.getClientId())
                    .orElseThrow(() -> new ClientNotFoundException(dechargementDTO.getClientId()));
        }
        
        // Dépôt est optionnel
        Depot depot = null;
        if (dechargementDTO.getDepotId() != null) {
            depot = depotRepository.findById(dechargementDTO.getDepotId())
                    .orElseThrow(() -> new DepotNotFoundException(dechargementDTO.getDepotId()));
        }

        Dechargement dechargement = new Dechargement();
        dechargement.setChargement(chargement);
        dechargement.setNumTicket(dechargementDTO.getNumTicket());
        dechargement.setNumBonLivraison(dechargementDTO.getNumBonLivraison());
        dechargement.setPoidCamionVide(dechargementDTO.getPoidCamionVide());
        dechargement.setPoidComplet(dechargementDTO.getPoidComplet());
        dechargement.setDateDechargement(dechargementDTO.getDateDechargement());
        dechargement.setClient(client);
        dechargement.setDepot(depot);
    // Propager le code d'autorisation si fourni
    dechargement.setAutorisationCode(dechargementDTO.getAutorisationCode());

        Dechargement savedDechargement = dechargementRepository.save(dechargement);

        // Créer un voyage depuis les données du déchargement
        createVoyageFromDechargement(savedDechargement);

        return dechargementMapper.toDTO(savedDechargement);
    }

    @Transactional
    public DechargementDTO updateDechargement(Long id, DechargementDTO dechargementDTO) {
        Dechargement dechargement = dechargementRepository.findById(id)
                .orElseThrow(() -> new DechargementNotFoundException(id));

        // Valider l'existence des entités liées
        if (dechargementDTO.getChargementId() != null) {
            Chargement chargement = chargementRepository.findById(dechargementDTO.getChargementId())
                    .orElseThrow(() -> new ChargementNotFoundException(dechargementDTO.getChargementId()));
            dechargement.setChargement(chargement);
        }

        if (dechargementDTO.getClientId() != null) {
            Client client = clientRepository.findById(dechargementDTO.getClientId())
                    .orElseThrow(() -> new ClientNotFoundException(dechargementDTO.getClientId()));
            dechargement.setClient(client);
        } else {
            // Si clientId est null, on supprime le client
            dechargement.setClient(null);
        }

        if (dechargementDTO.getDepotId() != null) {
            Depot depot = depotRepository.findById(dechargementDTO.getDepotId())
                    .orElseThrow(() -> new DepotNotFoundException(dechargementDTO.getDepotId()));
            dechargement.setDepot(depot);
        } else {
            // Si depotId est null, on supprime le dépôt
            dechargement.setDepot(null);
        }

        if (dechargementDTO.getNumTicket() != null) {
            dechargement.setNumTicket(dechargementDTO.getNumTicket());
        }

        if (dechargementDTO.getNumBonLivraison() != null) {
            dechargement.setNumBonLivraison(dechargementDTO.getNumBonLivraison());
        }

        // Mettre à jour les poids avec validation si fournis
        Double newVide = dechargementDTO.getPoidCamionVide();
        Double newBrut = dechargementDTO.getPoidComplet();
        if (newVide != null) {
            if (Math.floor(newVide) != newVide) {
                throw new IllegalArgumentException("Le poids tar doit être un entier");
            }
            dechargement.setPoidCamionVide(newVide);
        }

        if (newBrut != null) {
            if (Math.floor(newBrut) != newBrut) {
                throw new IllegalArgumentException("Le poids brut doit être un entier");
            }
            dechargement.setPoidComplet(newBrut);
        }

        // Si les deux poids sont présents (après mise à jour), valider brut > tar
        if (dechargement.getPoidCamionVide() != null && dechargement.getPoidComplet() != null) {
            if (dechargement.getPoidComplet() <= dechargement.getPoidCamionVide()) {
                throw new IllegalArgumentException("Le poids brut doit être strictement supérieur au poids tar");
            }
        }

        if (dechargementDTO.getDateDechargement() != null) {
            dechargement.setDateDechargement(dechargementDTO.getDateDechargement());
        }
        if (dechargementDTO.getAutorisationCode() != null) {
            dechargement.setAutorisationCode(dechargementDTO.getAutorisationCode());
        }

        Dechargement updatedDechargement = dechargementRepository.save(dechargement);
        return dechargementMapper.toDTO(updatedDechargement);
    }

    @Transactional
    public void deleteDechargement(Long id) {
        if (!dechargementRepository.existsById(id)) {
            throw new DechargementNotFoundException(id);
        }
        dechargementRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public DechargementDTO getDechargementByChargementId(Long chargementId) {
        Dechargement dechargement = dechargementRepository.findByChargementId(chargementId)
                .orElseThrow(() -> new DechargementNotFoundException("Dechargement not found for chargement id: " + chargementId));
        return dechargementMapper.toDTO(dechargement);
    }

    @Transactional(readOnly = true)
    public List<DechargementDTO> getDechargementsByClient(Long clientId) {
        return dechargementRepository.findByClientId(clientId)
                .stream()
                .map(dechargementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DechargementDTO> getDechargementsByDepot(Long depotId) {
        return dechargementRepository.findByDepotId(depotId)
                .stream()
                .map(dechargementMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crée un voyage depuis les données du déchargement
     */
    private void createVoyageFromDechargement(Dechargement dechargement) {
        Chargement chargement = dechargement.getChargement();
        
        Voyage voyage = new Voyage();
        voyage.setNumBonLivraison(dechargement.getNumBonLivraison());
        voyage.setNumTicket(dechargement.getNumTicket());
        // Utiliser la date du déchargement si fournie, sinon fallback à maintenant
        voyage.setDate(dechargement.getDateDechargement() != null ? dechargement.getDateDechargement() : LocalDateTime.now());
        voyage.setSociete(chargement.getSociete());
        // Propager également la société du projet (societeP) depuis le chargement
        voyage.setSocieteP(chargement.getSocieteP());
    // Propager le code d'autorisation depuis le déchargement (si présent)
    voyage.setAutorisationCode(dechargement.getAutorisationCode());
        
        // Relations depuis le chargement
        voyage.setCamion(chargement.getCamion());
        voyage.setChauffeur(chargement.getChauffeur());
        voyage.setProjet(chargement.getProjet());
        
        // Relations depuis le déchargement
        voyage.setClient(dechargement.getClient());
        voyage.setDepot(dechargement.getDepot());
        
        // Calcul de la quantité (poids complet - poids vide)
        Double quantite = 0.0;
        if (dechargement.getPoidComplet() != null && dechargement.getPoidCamionVide() != null) {
            quantite = dechargement.getPoidComplet() - dechargement.getPoidCamionVide();
        }
        voyage.setQuantite(quantite);
        
        // Initialiser le reste avec la quantité
        voyage.setReste(quantite);
        
        // Poids depot et client - Logique corrigée
        if (dechargement.getClient() != null) {
            // Si c'est un client, enregistrer dans poidsClient
            voyage.setPoidsClient(quantite);
            voyage.setPoidsDepot(0.0);
        } else if (dechargement.getDepot() != null) {
            // Si c'est un dépôt, enregistrer dans poidsDepot
            voyage.setPoidsDepot(quantite);
            voyage.setPoidsClient(0.0);
        } else {
            // Cas par défaut (ne devrait pas arriver)
            voyage.setPoidsDepot(0.0);
            voyage.setPoidsClient(0.0);
        }
        
        voyageRepository.save(voyage);
    }
}
