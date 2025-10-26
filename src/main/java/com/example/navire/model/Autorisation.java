package com.example.navire.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class Autorisation {

    @NotNull(message = "Le code d'autorisation est requis")
    private String code;

    @NotNull(message = "La quantité est requise")
    @PositiveOrZero(message = "La quantité doit être positive ou nulle")
    private Double quantite;
}
