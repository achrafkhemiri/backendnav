package com.example.navire.dto;
import java.util.Set;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CamionDTO {
    private Long id;

    @jakarta.validation.constraints.NotNull(message = "Le matricule est obligatoire")
    @jakarta.validation.constraints.Size(min = 3, max = 20, message = "Le matricule doit comporter entre 3 et 20 caractères")
    private String matricule;

    @jakarta.validation.constraints.NotNull(message = "La société est obligatoire")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "La société doit comporter entre 2 et 100 caractères")
    private String societe;

    private String numBonLivraison;
}
