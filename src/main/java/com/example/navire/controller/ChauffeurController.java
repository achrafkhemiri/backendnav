package com.example.navire.controller;

import com.example.navire.dto.ChauffeurDTO;
import com.example.navire.services.ChauffeurService;
import com.example.navire.exception.ChauffeurNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/chauffeurs")
public class ChauffeurController {
    @Autowired
    private ChauffeurService chauffeurService;

    @GetMapping
    public List<ChauffeurDTO> getAllChauffeurs() {
        return chauffeurService.getAllChauffeurs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChauffeurDTO> getChauffeurById(@PathVariable Long id) {
        return ResponseEntity.ok(chauffeurService.getChauffeurById(id));
    }

    @PostMapping
    public ResponseEntity<ChauffeurDTO> createChauffeur(@RequestBody @jakarta.validation.Valid ChauffeurDTO dto) {
        ChauffeurDTO created = chauffeurService.createChauffeur(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChauffeurDTO> updateChauffeur(@PathVariable Long id, @RequestBody @jakarta.validation.Valid ChauffeurDTO dto) {
        ChauffeurDTO updated = chauffeurService.updateChauffeur(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChauffeur(@PathVariable Long id) {
        chauffeurService.deleteChauffeur(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ChauffeurNotFoundException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        if (ex instanceof ChauffeurNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
