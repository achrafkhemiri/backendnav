package com.example.navire.controller;

import com.example.navire.dto.ProjetClientDTO;
import com.example.navire.exception.QuantiteDepassementException;
import com.example.navire.services.ProjetClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projet-client")
public class ProjetClientController {
    @Autowired
    private ProjetClientService projetClientService;

    @GetMapping
    public ResponseEntity<List<ProjetClientDTO>> getAllProjetClients() {
        List<ProjetClientDTO> projetClients = projetClientService.getAllProjetClients();
        return ResponseEntity.ok(projetClients);
    }

    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<ProjetClientDTO>> getProjetClientsByProjetId(@PathVariable Long projetId) {
        List<ProjetClientDTO> projetClients = projetClientService.getProjetClientsByProjetId(projetId);
        return ResponseEntity.ok(projetClients);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<ProjetClientDTO>> getProjetClientsByClientId(@PathVariable Long clientId) {
        List<ProjetClientDTO> projetClients = projetClientService.getProjetClientsByClientId(clientId);
        return ResponseEntity.ok(projetClients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjetClientDTO> getProjetClientById(@PathVariable Long id) {
        ProjetClientDTO projetClient = projetClientService.getProjetClientById(id);
        return ResponseEntity.ok(projetClient);
    }

    @PostMapping
    public ResponseEntity<ProjetClientDTO> createProjetClient(@Valid @RequestBody ProjetClientDTO projetClientDTO) {
        ProjetClientDTO created = projetClientService.createProjetClient(projetClientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}/quantite-autorisee")
    public ResponseEntity<ProjetClientDTO> updateQuantiteAutorisee(
            @PathVariable Long id, 
            @RequestBody Double quantiteAutorisee) {
        ProjetClientDTO updated = projetClientService.updateQuantiteAutorisee(id, quantiteAutorisee);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}/autorisation")
    public ResponseEntity<ProjetClientDTO> updateAutorisation(
            @PathVariable Long id,
            @RequestBody java.util.Set<com.example.navire.dto.AutorisationDTO> autorisation) {
        ProjetClientDTO updated = projetClientService.updateAutorisation(id, autorisation);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjetClient(@PathVariable Long id) {
        projetClientService.deleteProjetClient(id);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(QuantiteDepassementException.class)
    public ResponseEntity<Map<String, String>> handleQuantiteDepassement(QuantiteDepassementException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("message", ex.getMessage()));
    }
}
