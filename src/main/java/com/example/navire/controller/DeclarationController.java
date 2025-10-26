package com.example.navire.controller;

import com.example.navire.dto.DeclarationDTO;
import com.example.navire.services.DeclarationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/declarations")
@CrossOrigin(origins = "*")
public class DeclarationController {

    @Autowired
    private DeclarationService declarationService;

    // GET /api/declarations - Récupérer toutes les déclarations
    @GetMapping
    public ResponseEntity<List<DeclarationDTO>> getAllDeclarations() {
        List<DeclarationDTO> declarations = declarationService.getAllDeclarations();
        return ResponseEntity.ok(declarations);
    }

    // GET /api/declarations/{id} - Récupérer une déclaration par ID
    @GetMapping("/{id}")
    public ResponseEntity<DeclarationDTO> getDeclarationById(@PathVariable Long id) {
        DeclarationDTO declaration = declarationService.getDeclarationById(id);
        return ResponseEntity.ok(declaration);
    }

    // GET /api/declarations/projet/{projetId} - Récupérer les déclarations d'un projet
    @GetMapping("/projet/{projetId}")
    public ResponseEntity<List<DeclarationDTO>> getDeclarationsByProjet(@PathVariable Long projetId) {
        List<DeclarationDTO> declarations = declarationService.getDeclarationsByProjet(projetId);
        return ResponseEntity.ok(declarations);
    }

    // POST /api/declarations - Créer une déclaration
    @PostMapping
    public ResponseEntity<DeclarationDTO> createDeclaration(@Valid @RequestBody DeclarationDTO declarationDTO) {
        DeclarationDTO created = declarationService.createDeclaration(declarationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT /api/declarations/{id} - Mettre à jour une déclaration
    @PutMapping("/{id}")
    public ResponseEntity<DeclarationDTO> updateDeclaration(
            @PathVariable Long id,
            @Valid @RequestBody DeclarationDTO declarationDTO) {
        DeclarationDTO updated = declarationService.updateDeclaration(id, declarationDTO);
        return ResponseEntity.ok(updated);
    }

    // DELETE /api/declarations/{id} - Supprimer une déclaration
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeclaration(@PathVariable Long id) {
        declarationService.deleteDeclaration(id);
        return ResponseEntity.noContent().build();
    }
}
