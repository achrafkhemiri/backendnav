package com.example.navire.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "dechargement")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dechargement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "chargement_id", nullable = false, unique = true)
    private Chargement chargement;

    @Column(name = "num_ticket")
    private String numTicket;

    @Column(name = "num_bon_livraison")
    private String numBonLivraison;

    @Column(name = "poid_camion_vide")
    private Double poidCamionVide;

    @Column(name = "poid_complet")
    private Double poidComplet;

    @Column(name = "date_dechargement")
    private LocalDateTime dateDechargement;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = true)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "depot_id", nullable = true)
    private Depot depot;

    @Column(name = "autorisation_code")
    private String autorisationCode;
}
