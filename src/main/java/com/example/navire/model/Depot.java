package com.example.navire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Set;
import com.example.navire.model.ProjetDepot;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "depots")
public class Depot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom du dépôt est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom du dépôt doit comporter entre 2 et 100 caractères")
    @Column(nullable = false, unique = true)
    private String nom;

    @Size(max = 255, message = "L'adresse ne peut pas dépasser 255 caractères")
    @Column(length = 255)
    private String adresse;

    @Size(max = 50, message = "Le matricule fiscal ne peut pas dépasser 50 caractères")
    @Column(length = 50)
    private String mf;

    // Relation Depot <-> Projet (Many-to-Many via ProjetDepot)
    @OneToMany(mappedBy = "depot", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjetDepot> projetDepots;
}
