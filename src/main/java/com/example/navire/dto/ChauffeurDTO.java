package com.example.navire.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChauffeurDTO {
    private Long id;

    @jakarta.validation.constraints.NotNull(message = "Le nom du chauffeur est obligatoire")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Le nom du chauffeur doit comporter entre 2 et 100 caractères")
    private String nom;

    @jakarta.validation.constraints.NotNull(message = "Le numéro CIN est obligatoire")
    @jakarta.validation.constraints.Size(min = 8, max = 8, message = "Le numéro CIN doit comporter 8 chiffres")
    private String numCin;

    private java.util.Set<String> numBonLivraisonVoyages;
}
