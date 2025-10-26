package com.example.navire.model;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

// Lombok annotations
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "camions")
public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le matricule est obligatoire")
    @Size(min = 3, max = 20, message = "Le matricule doit comporter entre 3 et 20 caractères")
    @Column(nullable = false, unique = true)
    private String matricule;

    @NotNull(message = "La société est obligatoire")
    @Size(min = 2, max = 100, message = "La société doit comporter entre 2 et 100 caractères")
    @Column(nullable = false)
    private String societe;

    @OneToMany(mappedBy = "camion")
    private Set<Voyage> voyages = new HashSet<>();

    @Column(name = "num_bon_livraison")
    private String numBonLivraison;
}
