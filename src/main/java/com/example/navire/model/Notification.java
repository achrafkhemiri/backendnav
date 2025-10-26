package com.example.navire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NiveauAlerte niveau;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "entite_type")
    private String entiteType; // "PROJET", "CLIENT", "VOYAGE"
    
    @Column(name = "entite_id")
    private Long entiteId;
    
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;
    
    @Column(nullable = false)
    private boolean lu = false;
    
    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;
    
    @Column(nullable = false)
    private boolean deletable = true; // Par défaut, les notifications sont supprimables
    
    public enum TypeNotification {
        DEPASSEMENT_QUANTITE,
        QUANTITE_PROCHE_LIMITE,
        VALIDATION_CLIENT,
        VALIDATION_VOYAGE,
        ALERTE_STOCK,
        INFO_GENERALE
    }
    
    public enum NiveauAlerte {
        INFO,      // Bleu
        SUCCESS,   // Vert
        WARNING,   // Orange
        DANGER     // Rouge
    }
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}
