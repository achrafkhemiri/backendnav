package com.example.navire.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chargement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chargement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "camion_id", nullable = false)
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "chauffeur_id", nullable = false)
    private Chauffeur chauffeur;

    @Column(nullable = false)
    private String societe;

    // Société du projet (distincte du transporteur)
    @Column(name = "societe_p")
    private String societeP;

    @ManyToOne
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;

    @Column(name = "date_chargement", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateChargement;
}
