package com.example.navire.model;
import java.util.Set;
import java.util.HashSet;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "voyages")
public class Voyage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le numéro du bon de livraison est obligatoire")
    @Size(min = 2, max = 30, message = "Le numéro du bon de livraison doit comporter entre 2 et 30 caractères")
    @Column(name = "num_bon_livraison", nullable = false, unique = true)
    private String numBonLivraison;

    @NotNull(message = "Le numéro du ticket est obligatoire")
    @Size(min = 2, max = 30, message = "Le numéro du ticket doit comporter entre 2 et 30 caractères")
    @Column(name = "num_ticket", nullable = false, unique = true)
    private String numTicket;

    @NotNull(message = "Le reste est obligatoire")
    @Min(value = 0, message = "Le reste doit être positif")
    @Column(nullable = false)
    private Double reste;

    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDateTime date;

    @Column(name = "poids_client")
    private Double poidsClient;

    @Column(name = "poids_depot")
    private Double poidsDepot;

    @Size(max = 100, message = "Le nom de la société doit comporter au maximum 100 caractères")
    @Column(name = "societe", length = 100)
    private String societe;

    // Société du projet (distincte de la société de livraison/transporteur)
    @Size(max = 100)
    @Column(name = "societe_p", length = 100)
    private String societeP;

    // Relations
    @ManyToOne
    @JoinColumn(name = "chauffeur_id")
    private Chauffeur chauffeur;

    @NotNull(message = "Le camion est obligatoire")
    @ManyToOne
    @JoinColumn(name = "camion_id")
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "depot_id")
    private Depot depot;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "projet_client_id")
    private ProjetClient projetClient;

    // Ajout relation ManyToOne vers User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(nullable = false)
    private Double quantite = 0.0;

    @Column(name = "autorisation_code")
    private String autorisationCode;
}
