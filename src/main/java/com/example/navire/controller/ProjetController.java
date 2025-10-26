package com.example.navire.controller;

import com.example.navire.dto.ProjetDTO;
import com.example.navire.services.ProjetService;
import com.example.navire.exception.ProjetNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/projets")
public class ProjetController {
    @Autowired
    private ProjetService projetService;

    @PostMapping("/{projetId}/clients/{clientId}")
    public ResponseEntity<?> addClientToProjet(@PathVariable Long projetId, @PathVariable Long clientId,
                                               @RequestBody(required = false) java.util.Map<String, Object> body) {
        Double quantite = null;
        if (body != null && body.get("quantiteAutorisee") != null) {
            try {
                quantite = Double.valueOf(body.get("quantiteAutorisee").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("quantiteAutorisee must be a number");
            }
        }
        projetService.addClientToProjet(projetId, clientId, quantite);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projetId}/depots/{depotId}")
    public ResponseEntity<?> addDepotToProjet(
            @PathVariable Long projetId, 
            @PathVariable Long depotId,
            @RequestParam(required = false, defaultValue = "0.0") Double quantiteAutorisee) {
        projetService.addDepotToProjet(projetId, depotId, quantiteAutorisee);
        return ResponseEntity.ok().build();
    }

    /**
     * Récupère tous les dépôts associés à un projet spécifique
     */
    @GetMapping("/{projetId}/depots")
    public ResponseEntity<List<com.example.navire.dto.DepotDTO>> getDepotsByProjet(@PathVariable Long projetId) {
        java.util.List<com.example.navire.model.Depot> depots = projetService.getDepotsByProjetId(projetId);
        java.util.List<com.example.navire.dto.DepotDTO> dtos = depots.stream()
                .map(depot -> {
                    com.example.navire.dto.DepotDTO dto = new com.example.navire.dto.DepotDTO();
                    dto.setId(depot.getId());
                    dto.setNom(depot.getNom());
                    dto.setAdresse(depot.getAdresse());
                    dto.setMf(depot.getMf());
                    dto.setProjetId(projetId); // Associer le projet ID
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Ajoute une société à un projet
     */
    @PostMapping("/{projetId}/societes/{societeId}")
    public ResponseEntity<?> addSocieteToProjet(@PathVariable Long projetId, @PathVariable Long societeId) {
        projetService.addSocieteToProjet(projetId, societeId);
        return ResponseEntity.ok().build();
    }

    /**
     * Retire une société d'un projet
     */
    @DeleteMapping("/{projetId}/societes/{societeId}")
    public ResponseEntity<?> removeSocieteFromProjet(@PathVariable Long projetId, @PathVariable Long societeId) {
        projetService.removeSocieteFromProjet(projetId, societeId);
        return ResponseEntity.ok().build();
    }

    /**
     * Récupère toutes les sociétés associées à un projet
     */
    @GetMapping("/{projetId}/societes")
    public ResponseEntity<List<com.example.navire.dto.SocieteDTO>> getSocietesByProjet(@PathVariable Long projetId) {
        java.util.List<com.example.navire.model.Societe> societes = projetService.getSocietesByProjetId(projetId);
        java.util.List<com.example.navire.dto.SocieteDTO> dtos = societes.stream()
                .map(societe -> {
                    com.example.navire.dto.SocieteDTO dto = new com.example.navire.dto.SocieteDTO();
                    dto.setId(societe.getId());
                    dto.setNom(societe.getNom());
                    dto.setAdresse(societe.getAdresse());
                    dto.setRcs(societe.getRcs());
                    dto.setContact(societe.getContact());
                    dto.setTva(societe.getTva());
                    return dto;
                })
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping
    public List<ProjetDTO> getAllProjets() {
        return projetService.getAllProjets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetDTO> getProjetById(@PathVariable Long id) {
        return ResponseEntity.ok(projetService.getProjetById(id));
    }

    @PostMapping
    public ResponseEntity<ProjetDTO> createProjet(@RequestBody @jakarta.validation.Valid ProjetDTO dto) {
        ProjetDTO created = projetService.createProjet(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetDTO> updateProjet(@PathVariable Long id, @RequestBody @jakarta.validation.Valid ProjetDTO dto) {
        ProjetDTO updated = projetService.updateProjet(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjet(@PathVariable Long id) {
        projetService.deleteProjet(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ProjetNotFoundException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        if (ex instanceof ProjetNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
