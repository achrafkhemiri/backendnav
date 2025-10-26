
package com.example.navire.model;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "projet_depot")
public class ProjetDepot implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projet_id")
    @NotNull(message = "Projet is required")
    private Projet projet;

    @ManyToOne
    @JoinColumn(name = "depot_id")
    @NotNull(message = "Depot is required")
    private Depot depot;

    @NotNull(message = "Quantite autorisee is required")
    @PositiveOrZero(message = "Quantite autorisee must be zero or positive")
    private Double quantiteAutorisee;
}
