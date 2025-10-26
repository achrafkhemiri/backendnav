package com.example.navire.services;

import com.example.navire.model.Notification;
import com.example.navire.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    
    /**
     * Types de notifications
     */
    public enum TypeNotification {
        DEPASSEMENT_QUANTITE,
        QUANTITE_PROCHE_LIMITE,
        VALIDATION_CLIENT,
        VALIDATION_VOYAGE,
        ALERTE_STOCK,
        INFO_GENERALE
    }
    
    /**
     * Niveaux d'alerte
     */
    public enum NiveauAlerte {
        INFO,      // Bleu
        SUCCESS,   // Vert
        WARNING,   // Orange
        DANGER     // Rouge
    }
    
    /**
     * Crée une nouvelle notification
     */
    @Transactional
    public Notification creerNotification(
            TypeNotification type,
            NiveauAlerte niveau,
            String message,
            String entiteType,
            Long entiteId
    ) {
        Notification notification = new Notification();
        notification.setType(Notification.TypeNotification.valueOf(type.name()));
        notification.setNiveau(Notification.NiveauAlerte.valueOf(niveau.name()));
        notification.setMessage(message);
        notification.setEntiteType(entiteType);
        notification.setEntiteId(entiteId);
        notification.setLu(false);
        
        return notificationRepository.save(notification);
    }
    
    /**
     * Crée une notification à partir d'un objet Notification complet
     */
    @Transactional
    public Notification creerNotification(Notification notification) {
        if (notification.getDateCreation() == null) {
            notification.setDateCreation(java.time.LocalDateTime.now());
        }
        return notificationRepository.save(notification);
    }
    
    /**
     * Crée une notification simple sans entité liée
     */
    @Transactional
    public Notification creerNotificationSimple(
            TypeNotification type,
            NiveauAlerte niveau,
            String message
    ) {
        return creerNotification(type, niveau, message, null, null);
    }
    
    /**
     * Récupère toutes les notifications non lues
     */
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsNonLues() {
        return notificationRepository.findByLuFalseOrderByDateCreationDesc();
    }
    
    /**
     * Récupère toutes les notifications
     */
    @Transactional(readOnly = true)
    public List<Notification> getToutesLesNotifications() {
        return notificationRepository.findAllByOrderByDateCreationDesc();
    }
    
    /**
     * Récupère les notifications par niveau
     */
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsParNiveau(NiveauAlerte niveau) {
        return notificationRepository.findByNiveauOrderByDateCreationDesc(
            Notification.NiveauAlerte.valueOf(niveau.name())
        );
    }
    
    /**
     * Récupère les notifications pour une entité spécifique
     */
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsParEntite(String entiteType, Long entiteId) {
        return notificationRepository.findByEntiteTypeAndEntiteIdOrderByDateCreationDesc(
            entiteType, entiteId
        );
    }
    
    /**
     * Marque une notification comme lue
     */
    @Transactional
    public Notification marquerCommeLue(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        
        if (!notification.isLu()) {
            notification.setLu(true);
            notification.setDateLecture(LocalDateTime.now());
            return notificationRepository.save(notification);
        }
        
        return notification;
    }
    
    /**
     * Marque toutes les notifications comme lues
     */
    @Transactional
    public void marquerToutesCommeLues() {
        List<Notification> notifications = notificationRepository.findByLuFalseOrderByDateCreationDesc();
        notifications.forEach(n -> {
            n.setLu(true);
            n.setDateLecture(LocalDateTime.now());
        });
        notificationRepository.saveAll(notifications);
    }
    
    /**
     * Compte le nombre de notifications non lues
     */
    @Transactional(readOnly = true)
    public Long compterNotificationsNonLues() {
        return notificationRepository.countNonLues();
    }
    
    /**
     * Compte les notifications non lues par niveau
     */
    @Transactional(readOnly = true)
    public Long compterNotificationsNonLuesParNiveau(NiveauAlerte niveau) {
        return notificationRepository.countNonLuesByNiveau(
            Notification.NiveauAlerte.valueOf(niveau.name())
        );
    }
    
    /**
     * Récupère les statistiques des notifications
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getStatistiquesNotifications() {
        Map<String, Long> stats = new HashMap<>();
        
        stats.put("total", notificationRepository.count());
        stats.put("nonLues", compterNotificationsNonLues());
        stats.put("danger", compterNotificationsNonLuesParNiveau(NiveauAlerte.DANGER));
        stats.put("warning", compterNotificationsNonLuesParNiveau(NiveauAlerte.WARNING));
        stats.put("info", compterNotificationsNonLuesParNiveau(NiveauAlerte.INFO));
        stats.put("success", compterNotificationsNonLuesParNiveau(NiveauAlerte.SUCCESS));
        
        return stats;
    }
    
    /**
     * Supprime une notification (seulement si elle est supprimable)
     */
    @Transactional
    public void supprimerNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'ID: " + notificationId));
        
        if (!notification.isDeletable()) {
            throw new RuntimeException("Cette notification ne peut pas être supprimée car elle est critique pour la traçabilité");
        }
        
        notificationRepository.deleteById(notificationId);
    }
    
    /**
     * Supprime toutes les notifications lues (seulement celles qui sont supprimables)
     */
    @Transactional
    public void supprimerNotificationsLues() {
        List<Notification> notifications = notificationRepository.findAllByOrderByDateCreationDesc();
        List<Notification> lues = notifications.stream()
                .filter(Notification::isLu)
                .filter(Notification::isDeletable) // Ne supprimer que les notifications supprimables
                .toList();
        notificationRepository.deleteAll(lues);
    }
    
    /**
     * Supprime les notifications anciennes (plus de 30 jours)
     */
    @Transactional
    public void nettoyerNotificationsAnciennes() {
        LocalDateTime dateLimit = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteByDateCreationBefore(dateLimit);
    }
}
