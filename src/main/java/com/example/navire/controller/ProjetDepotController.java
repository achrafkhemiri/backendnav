package com.example.navire.controller;

import com.example.navire.dto.ProjetDepotDTO;
import com.example.navire.exception.QuantiteDepassementException;
import com.example.navire.services.ProjetDepotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projet-depot")
public class ProjetDepotController {
    @Autowired
    private ProjetDepotService projetDepotService;

    @GetMapping
    public ResponseEntity<List<ProjetDepotDTO>> getAllProjetDepots() {
        List<ProjetDepotDTO> projetDepots = projetDepotService.getAllProjetDepots();
        return ResponseEntity.ok(projetDepots);
    }

    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<ProjetDepotDTO>> getProjetDepotsByProjetId(@PathVariable Long projetId) {
        List<ProjetDepotDTO> projetDepots = projetDepotService.getProjetDepotsByProjetId(projetId);
        return ResponseEntity.ok(projetDepots);
    }

    @GetMapping("/depot/{depotId}")
    public ResponseEntity<List<ProjetDepotDTO>> getProjetDepotsByDepotId(@PathVariable Long depotId) {
        List<ProjetDepotDTO> projetDepots = projetDepotService.getProjetDepotsByDepotId(depotId);
        return ResponseEntity.ok(projetDepots);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetDepotDTO> getProjetDepotById(@PathVariable Long id) {
        ProjetDepotDTO projetDepot = projetDepotService.getProjetDepotById(id);
        return ResponseEntity.ok(projetDepot);
    }

    @PostMapping
    public ResponseEntity<ProjetDepotDTO> createProjetDepot(@Valid @RequestBody ProjetDepotDTO projetDepotDTO) {
        ProjetDepotDTO created = projetDepotService.createProjetDepot(projetDepotDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/quantite-autorisee")
    public ResponseEntity<ProjetDepotDTO> updateQuantiteAutorisee(
            @PathVariable Long id, 
            @RequestBody Double quantiteAutorisee) {
        ProjetDepotDTO updated = projetDepotService.updateQuantiteAutorisee(id, quantiteAutorisee);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjetDepot(@PathVariable Long id) {
        projetDepotService.deleteProjetDepot(id);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(QuantiteDepassementException.class)
    public ResponseEntity<Map<String, String>> handleQuantiteDepassement(QuantiteDepassementException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", ex.getMessage()));
    }
}
