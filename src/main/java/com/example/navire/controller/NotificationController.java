package com.example.navire.controller;

import com.example.navire.model.Notification;
import com.example.navire.services.NotificationService;
import com.example.navire.services.QuantiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {
    
    private final NotificationService notificationService;
    private final QuantiteService quantiteService;
    
    /**
     * Récupère toutes les notifications
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getToutesLesNotifications() {
        return ResponseEntity.ok(notificationService.getToutesLesNotifications());
    }
    
    /**
     * Crée une nouvelle notification
     */
    @PostMapping
    public ResponseEntity<Notification> creerNotification(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.creerNotification(notification));
    }
    
    /**
     * Récupère les notifications non lues
     */
    @GetMapping("/non-lues")
    public ResponseEntity<List<Notification>> getNotificationsNonLues() {
        return ResponseEntity.ok(notificationService.getNotificationsNonLues());
    }
    
    /**
     * Récupère les notifications par niveau
     */
    @GetMapping("/niveau/{niveau}")
    public ResponseEntity<List<Notification>> getNotificationsParNiveau(
            @PathVariable NotificationService.NiveauAlerte niveau) {
        return ResponseEntity.ok(notificationService.getNotificationsParNiveau(niveau));
    }
    
    /**
     * Récupère les notifications pour une entité
     */
    @GetMapping("/entite/{type}/{id}")
    public ResponseEntity<List<Notification>> getNotificationsParEntite(
            @PathVariable String type,
            @PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationsParEntite(type, id));
    }
    
    /**
     * Marque une notification comme lue
     */
    @PutMapping("/{id}/lue")
    public ResponseEntity<Notification> marquerCommeLue(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.marquerCommeLue(id));
    }
    
    /**
     * Marque toutes les notifications comme lues
     */
    @PutMapping("/marquer-toutes-lues")
    public ResponseEntity<Void> marquerToutesCommeLues() {
        notificationService.marquerToutesCommeLues();
        return ResponseEntity.ok().build();
    }
    
    /**
     * Supprime une notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerNotification(@PathVariable Long id) {
        notificationService.supprimerNotification(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Supprime toutes les notifications lues
     */
    @DeleteMapping("/lues")
    public ResponseEntity<Void> supprimerNotificationsLues() {
        notificationService.supprimerNotificationsLues();
        return ResponseEntity.ok().build();
    }
    
    /**
     * Récupère les statistiques des notifications
     */
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Long>> getStatistiquesNotifications() {
        return ResponseEntity.ok(notificationService.getStatistiquesNotifications());
    }
    
    /**
     * Compte les notifications non lues
     */
    @GetMapping("/count/non-lues")
    public ResponseEntity<Long> compterNotificationsNonLues() {
        return ResponseEntity.ok(notificationService.compterNotificationsNonLues());
    }
}
