package com.example.navire.controller;

import com.example.navire.dto.SocieteDTO;
import com.example.navire.services.SocieteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/societes")
public class SocieteController {
    
    @Autowired
    private SocieteService societeService;

    @GetMapping
    public ResponseEntity<List<SocieteDTO>> getAllSocietes() {
        return ResponseEntity.ok(societeService.getAllSocietes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SocieteDTO> getSocieteById(@PathVariable Long id) {
        return ResponseEntity.ok(societeService.getSocieteById(id));
    }

    @PostMapping
    public ResponseEntity<SocieteDTO> createSociete(@RequestBody @jakarta.validation.Valid SocieteDTO dto) {
        SocieteDTO created = societeService.createSociete(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SocieteDTO> updateSociete(@PathVariable Long id, @RequestBody @jakarta.validation.Valid SocieteDTO dto) {
        SocieteDTO updated = societeService.updateSociete(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSociete(@PathVariable Long id) {
        societeService.deleteSociete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> handleException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
