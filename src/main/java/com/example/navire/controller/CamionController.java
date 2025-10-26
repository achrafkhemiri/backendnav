package com.example.navire.controller;

import com.example.navire.dto.CamionDTO;
import com.example.navire.services.CamionService;
import com.example.navire.exception.CamionNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/camions")
public class CamionController {
    @Autowired
    private CamionService camionService;

    @GetMapping
    public List<CamionDTO> getAllCamions() {
        return camionService.getAllCamions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamionDTO> getCamionById(@PathVariable Long id) {
        return ResponseEntity.ok(camionService.getCamionById(id));
    }

    @PostMapping
    public ResponseEntity<CamionDTO> createCamion(@RequestBody @jakarta.validation.Valid CamionDTO dto) {
        CamionDTO created = camionService.createCamion(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamionDTO> updateCamion(@PathVariable Long id, @RequestBody @jakarta.validation.Valid CamionDTO dto) {
        CamionDTO updated = camionService.updateCamion(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCamion(@PathVariable Long id) {
        camionService.deleteCamion(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({CamionNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        if (ex instanceof CamionNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
