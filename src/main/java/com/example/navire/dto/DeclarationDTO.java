package com.example.navire.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DeclarationDTO {
    private Long id;

    @jakarta.validation.constraints.NotNull(message = "Le numéro de déclaration est obligatoire")
    @jakarta.validation.constraints.Size(min = 1, max = 100, message = "Le numéro de déclaration doit comporter entre 1 et 100 caractères")
    private String numeroDeclaration;

    @jakarta.validation.constraints.NotNull(message = "La quantité manifestée est obligatoire")
    @jakarta.validation.constraints.Min(value = 0, message = "La quantité manifestée doit être positive")
    private Double quantiteManifestee;

    @jakarta.validation.constraints.NotNull(message = "L'ID du projet est obligatoire")
    private Long projetId;
}
