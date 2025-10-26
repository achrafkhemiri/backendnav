package com.example.navire.controller;

import com.example.navire.dto.DechargementDTO;
import com.example.navire.services.DechargementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dechargements")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DechargementController {

    private final DechargementService dechargementService;

    @GetMapping
    public ResponseEntity<List<DechargementDTO>> getAllDechargements() {
        List<DechargementDTO> dechargements = dechargementService.getAllDechargements();
        return ResponseEntity.ok(dechargements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DechargementDTO> getDechargementById(@PathVariable Long id) {
        DechargementDTO dechargement = dechargementService.getDechargementById(id);
        return ResponseEntity.ok(dechargement);
    }

    @PostMapping
    public ResponseEntity<DechargementDTO> createDechargement(@RequestBody DechargementDTO dechargementDTO) {
        DechargementDTO createdDechargement = dechargementService.createDechargement(dechargementDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDechargement);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DechargementDTO> updateDechargement(
            @PathVariable Long id,
            @RequestBody DechargementDTO dechargementDTO) {
        DechargementDTO updatedDechargement = dechargementService.updateDechargement(id, dechargementDTO);
        return ResponseEntity.ok(updatedDechargement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDechargement(@PathVariable Long id) {
        dechargementService.deleteDechargement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/chargement/{chargementId}")
    public ResponseEntity<DechargementDTO> getDechargementByChargementId(@PathVariable Long chargementId) {
        DechargementDTO dechargement = dechargementService.getDechargementByChargementId(chargementId);
        return ResponseEntity.ok(dechargement);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<DechargementDTO>> getDechargementsByClient(@PathVariable Long clientId) {
        List<DechargementDTO> dechargements = dechargementService.getDechargementsByClient(clientId);
        return ResponseEntity.ok(dechargements);
    }

    @GetMapping("/depot/{depotId}")
    public ResponseEntity<List<DechargementDTO>> getDechargementsByDepot(@PathVariable Long depotId) {
        List<DechargementDTO> dechargements = dechargementService.getDechargementsByDepot(depotId);
        return ResponseEntity.ok(dechargements);
    }
}
