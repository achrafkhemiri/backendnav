package com.example.navire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "societes")
public class Societe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom de la société est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom de la société doit comporter entre 2 et 100 caractères")
    @Column(name = "nom", unique = true)
    private String nom;

    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    @Column(name = "adresse", length = 255)
    private String adresse;

    @Size(max = 50, message = "Le RCS ne doit pas dépasser 50 caractères")
    @Column(name = "rcs", length = 50)
    private String rcs;

    @Size(max = 100, message = "Le contact ne doit pas dépasser 100 caractères")
    @Column(name = "contact", length = 100)
    private String contact;

    @Size(max = 50, message = "Le numéro de TVA ne doit pas dépasser 50 caractères")
    @Column(name = "tva", length = 50)
    private String tva;

    // Relation Many-to-Many avec Projet
    @ManyToMany(mappedBy = "societes")
    @ToString.Exclude
    private Set<Projet> projets;
}
