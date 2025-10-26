package com.example.navire.repository;

import com.example.navire.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByLuFalseOrderByDateCreationDesc();
    
    List<Notification> findAllByOrderByDateCreationDesc();
    
    List<Notification> findByNiveauOrderByDateCreationDesc(Notification.NiveauAlerte niveau);
    
    List<Notification> findByEntiteTypeAndEntiteIdOrderByDateCreationDesc(String entiteType, Long entiteId);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.lu = false")
    Long countNonLues();
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.lu = false AND n.niveau = :niveau")
    Long countNonLuesByNiveau(Notification.NiveauAlerte niveau);
    
    void deleteByDateCreationBefore(LocalDateTime date);
}
