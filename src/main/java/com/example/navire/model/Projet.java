package com.example.navire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "projets")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom du produit doit comporter entre 2 et 100 caractères")
    @Column(name = "nom_produit")
    private String nomProduit;

    @NotNull(message = "La quantité totale est obligatoire")
    @Min(value = 0, message = "La quantité totale doit être positive")
    @Column(name = "quantite_totale")
    private Double quantiteTotale;

    @NotNull(message = "Le nom du navire est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom du navire doit comporter entre 2 et 100 caractères")
    @Column(name = "nom_navire")
    private String nomNavire;

    @NotNull(message = "Le pays du navire est obligatoire")
    @Size(min = 2, max = 100, message = "Le pays du navire doit comporter entre 2 et 100 caractères")
    @Column(name = "pays_navire")
    private String paysNavire;

    @NotNull(message = "L'état du projet est obligatoire")
    @Size(min = 2, max = 50, message = "L'état doit comporter entre 2 et 50 caractères")
    @Column(name = "etat")
    private String etat;

    @Size(max = 100, message = "Le port ne doit pas dépasser 100 caractères")
    @Column(name = "port")
    private String port;

    // Dates optionnelles
    @Column(name = "date_debut")
    private java.time.LocalDate dateDebut;

    @Column(name = "date_fin")
    private java.time.LocalDate dateFin;

    // Projet actif ou non
    @Column(name = "active")
    private Boolean active = false;

    // Relation : un projet peut avoir plusieurs voyages
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Voyage> voyages;

    // Relation Projet <-> Client (Many-to-Many via ProjetClient)
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjetClient> projetClients;

    // Relation Projet <-> Depot (Many-to-Many via ProjetDepot)
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjetDepot> projetDepots;

    // Relation Projet <-> Societe (Many-to-Many)
    @ManyToMany
    @JoinTable(
        name = "projet_societe",
        joinColumns = @JoinColumn(name = "projet_id"),
        inverseJoinColumns = @JoinColumn(name = "societe_id")
    )
    private Set<Societe> societes;

    // Relation : un projet peut avoir plusieurs déclarations
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Declaration> declarations;
}
