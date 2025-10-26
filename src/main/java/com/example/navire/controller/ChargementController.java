package com.example.navire.controller;

import com.example.navire.dto.ChargementDTO;
import com.example.navire.services.ChargementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chargements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChargementController {

    private final ChargementService chargementService;

    @GetMapping
    public ResponseEntity<List<ChargementDTO>> getAllChargements() {
        List<ChargementDTO> chargements = chargementService.getAllChargements();
        return ResponseEntity.ok(chargements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChargementDTO> getChargementById(@PathVariable Long id) {
        ChargementDTO chargement = chargementService.getChargementById(id);
        return ResponseEntity.ok(chargement);
    }

    @PostMapping
    public ResponseEntity<ChargementDTO> createChargement(@RequestBody ChargementDTO chargementDTO) {
        ChargementDTO createdChargement = chargementService.createChargement(chargementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChargement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChargementDTO> updateChargement(
            @PathVariable Long id,
            @RequestBody ChargementDTO chargementDTO) {
        ChargementDTO updatedChargement = chargementService.updateChargement(id, chargementDTO);
        return ResponseEntity.ok(updatedChargement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChargement(@PathVariable Long id) {
        chargementService.deleteChargement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/camion/{camionId}")
    public ResponseEntity<List<ChargementDTO>> getChargementsByCamion(@PathVariable Long camionId) {
        List<ChargementDTO> chargements = chargementService.getChargementsByCamion(camionId);
        return ResponseEntity.ok(chargements);
    }

    @GetMapping("/chauffeur/{chauffeurId}")
    public ResponseEntity<List<ChargementDTO>> getChargementsByChauffeur(@PathVariable Long chauffeurId) {
        List<ChargementDTO> chargements = chargementService.getChargementsByChauffeur(chauffeurId);
        return ResponseEntity.ok(chargements);
    }

    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<ChargementDTO>> getChargementsByProjet(@PathVariable Long projetId) {
        List<ChargementDTO> chargements = chargementService.getChargementsByProjet(projetId);
        return ResponseEntity.ok(chargements);
    }

    @GetMapping("/societe/{societe}")
    public ResponseEntity<List<ChargementDTO>> getChargementsBySociete(@PathVariable String societe) {
        List<ChargementDTO> chargements = chargementService.getChargementsBySociete(societe);
        return ResponseEntity.ok(chargements);
    }
}
