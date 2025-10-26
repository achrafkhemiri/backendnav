package com.example.navire.controller;

import com.example.navire.services.QuantiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/quantites")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuantiteController {
    
    private final QuantiteService quantiteService;
    
    /**
     * Récupère les statistiques d'un projet
     */
    @GetMapping("/projet/{projetId}/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiquesProjet(@PathVariable Long projetId) {
        return ResponseEntity.ok(quantiteService.getStatistiquesProjet(projetId));
    }
    
    /**
     * Récupère les statistiques d'un client dans un projet
     */
    @GetMapping("/projet-client/{projetClientId}/statistiques")
    public ResponseEntity<Map<String, Double>> getStatistiquesClient(@PathVariable Long projetClientId) {
        return ResponseEntity.ok(quantiteService.getStatistiquesClient(projetClientId));
    }
    
    /**
     * Calcule la quantité restante d'un projet
     */
    @GetMapping("/projet/{projetId}/restante")
    public ResponseEntity<Double> getQuantiteRestante(@PathVariable Long projetId) {
        return ResponseEntity.ok(quantiteService.calculerQuantiteRestante(projetId));
    }
    
    /**
     * Calcule le reste pour un client
     */
    @GetMapping("/projet-client/{projetClientId}/reste")
    public ResponseEntity<Double> getResteClient(@PathVariable Long projetClientId) {
        return ResponseEntity.ok(quantiteService.calculerResteClient(projetClientId));
    }
    
    /**
     * Valide si une quantité peut être ajoutée au projet
     */
    @GetMapping("/projet/{projetId}/peut-ajouter/{quantite}")
    public ResponseEntity<Boolean> peutAjouterQuantite(
            @PathVariable Long projetId,
            @PathVariable double quantite) {
        return ResponseEntity.ok(quantiteService.peutAjouterQuantite(projetId, quantite));
    }
    
    /**
     * Valide l'ajout d'un client
     */
    @PostMapping("/projet/{projetId}/valider-client/{quantite}")
    public ResponseEntity<QuantiteService.ValidationResult> validerAjoutClient(
            @PathVariable Long projetId,
            @PathVariable double quantite) {
        return ResponseEntity.ok(quantiteService.validerAjoutClient(projetId, quantite));
    }
    
    /**
     * Valide l'ajout d'un voyage
     */
    @PostMapping("/projet-client/{projetClientId}/valider-voyage/{quantite}")
    public ResponseEntity<QuantiteService.ValidationResult> validerAjoutVoyage(
            @PathVariable Long projetClientId,
            @PathVariable double quantite) {
        return ResponseEntity.ok(quantiteService.validerAjoutVoyage(projetClientId, quantite));
    }
}
