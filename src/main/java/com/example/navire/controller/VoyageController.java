
package com.example.navire.controller;

import com.example.navire.dto.VoyageDTO;
import com.example.navire.services.VoyageService;
import com.example.navire.exception.VoyageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/voyages")
public class VoyageController {
    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<VoyageDTO>> getVoyagesByProjet(@PathVariable Long projetId) {
        if (!voyageService.projetExists(projetId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<VoyageDTO> voyages = voyageService.getVoyagesByProjetId(projetId);
        return ResponseEntity.ok(voyages);
    }
    @Autowired
    private VoyageService voyageService;

    @GetMapping
    public List<VoyageDTO> getAllVoyages() {
        return voyageService.getAllVoyages();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoyageDTO> getVoyageById(@PathVariable Long id) {
        return ResponseEntity.ok(voyageService.getVoyageById(id));
    }


    @PostMapping
    public ResponseEntity<?> createVoyage(@Valid @RequestBody VoyageDTO dto) {
        String validation = validateVoyageDto(dto, false);
        if (validation != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validation);
        }
        // Valeur par défaut si non fournie
        if (dto.getReste() == null) dto.setReste(0d);
        VoyageDTO created = voyageService.createVoyage(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateVoyage(@PathVariable Long id, @Valid @RequestBody VoyageDTO dto) {
        String validation = validateVoyageDto(dto, true);
        if (validation != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validation);
        }
        if (dto.getReste() == null) dto.setReste(0d);
        VoyageDTO updated = voyageService.updateVoyage(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoyage(@PathVariable Long id) {
        voyageService.deleteVoyage(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({VoyageNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        if (ex instanceof VoyageNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Règles métier:
     *  - camionId, chauffeurId, projetId, userId obligatoires
     *  - EXACTEMENT un parmi clientId, depotId (XOR)
     *  - reste optionnel (0 par défaut) côté controller
     */
    private String validateVoyageDto(VoyageDTO dto, boolean isUpdate) {
        StringBuilder errors = new StringBuilder();
        if (dto.getCamionId() == null) errors.append("camionId manquant. ");
        if (dto.getChauffeurId() == null) errors.append("chauffeurId manquant. ");
        if (dto.getProjetId() == null) errors.append("projetId manquant. ");
        if (dto.getUserId() == null) errors.append("userId manquant. ");

        boolean hasClient = dto.getClientId() != null;
        boolean hasDepot = dto.getDepotId() != null;
        if (hasClient && hasDepot) {
            errors.append("Ne pas fournir les deux: clientId et depotId (choisir un seul). ");
        } else if (!hasClient && !hasDepot) {
            errors.append("Fournir clientId OU depotId (au moins un). ");
        }

        return errors.length() == 0 ? null : errors.toString();
    }
}
