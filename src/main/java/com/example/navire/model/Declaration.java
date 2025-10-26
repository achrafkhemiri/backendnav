package com.example.navire.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "declarations")
public class Declaration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le numéro de déclaration est obligatoire")
    @Size(min = 1, max = 100, message = "Le numéro de déclaration doit comporter entre 1 et 100 caractères")
    @Column(name = "numero_declaration", nullable = false)
    private String numeroDeclaration;

    @NotNull(message = "La quantité manifestée est obligatoire")
    @Min(value = 0, message = "La quantité manifestée doit être positive")
    @Column(name = "quantite_manifestee", nullable = false)
    private Double quantiteManifestee;

    // Relation Many-to-One avec Projet
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projet_id", nullable = false)
    private Projet projet;
}
