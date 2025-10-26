package com.example.navire.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SocieteDTO {
    private Long id;

    @jakarta.validation.constraints.NotNull(message = "Le nom de la société est obligatoire")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Le nom de la société doit comporter entre 2 et 100 caractères")
    private String nom;

    private String adresse;

    private String rcs;

    private String contact;

    private String tva;
}
