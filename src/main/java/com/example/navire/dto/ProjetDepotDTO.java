package com.example.navire.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjetDepotDTO {
    
    private Long id;

    @NotNull(message = "Projet ID is required")
    private Long projetId;

    @NotNull(message = "Depot ID is required")
    private Long depotId;

    @NotNull(message = "Quantite autorisee is required")
    @PositiveOrZero(message = "Quantite autorisee must be zero or positive")
    private Double quantiteAutorisee;

    // Informations supplémentaires pour l'affichage
    private String depotNom;
    private String projetNom;
}
