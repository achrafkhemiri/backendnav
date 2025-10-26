package com.example.navire.model;
import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "chauffeurs")
public class Chauffeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom du chauffeur est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom du chauffeur doit comporter entre 2 et 100 caractères")
    @Column(nullable = false)
    private String nom;

    @NotNull(message = "Le numéro CIN est obligatoire")
    @Size(min = 8, max = 8, message = "Le numéro CIN doit comporter 8 chiffres")
    @Column(name = "num_cin", nullable = false, unique = true, length = 8)
    private String numCin;

    @OneToMany(mappedBy = "chauffeur")
    private Set<Voyage> voyages = new HashSet<>();
}
