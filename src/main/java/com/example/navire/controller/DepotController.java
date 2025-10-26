package com.example.navire.controller;

import com.example.navire.dto.DepotDTO;
import com.example.navire.services.DepotService;
import com.example.navire.exception.DepotNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/depots")
public class DepotController {
    @Autowired
    private DepotService depotService;

    @GetMapping
    public List<DepotDTO> getAllDepots() {
        return depotService.getAllDepots();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepotDTO> getDepotById(@PathVariable Long id) {
        return ResponseEntity.ok(depotService.getDepotById(id));
    }

    @PostMapping
    public ResponseEntity<DepotDTO> createDepot(@RequestBody DepotDTO dto) {
        DepotDTO created = depotService.createDepot(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DepotDTO> updateDepot(@PathVariable Long id, @RequestBody DepotDTO dto) {
        DepotDTO updated = depotService.updateDepot(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepot(@PathVariable Long id) {
        depotService.deleteDepot(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({DepotNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        if (ex instanceof DepotNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
