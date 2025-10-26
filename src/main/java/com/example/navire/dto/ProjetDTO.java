package com.example.navire.dto;

import lombok.*;
import java.util.Set;
import com.example.navire.dto.SocieteDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjetDTO {
    private Long id;

    @jakarta.validation.constraints.NotNull(message = "Le nom du produit est obligatoire")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Le nom du produit doit comporter entre 2 et 100 caractères")
    private String nomProduit;

    @jakarta.validation.constraints.NotNull(message = "La quantité totale est obligatoire")
    @jakarta.validation.constraints.Min(value = 0, message = "La quantité totale doit être positive")
    private Double quantiteTotale;

    @jakarta.validation.constraints.NotNull(message = "Le nom du navire est obligatoire")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Le nom du navire doit comporter entre 2 et 100 caractères")
    private String nomNavire;

    @jakarta.validation.constraints.NotNull(message = "Le pays du navire est obligatoire")
    @jakarta.validation.constraints.Size(min = 2, max = 100, message = "Le pays du navire doit comporter entre 2 et 100 caractères")
    private String paysNavire;

    @jakarta.validation.constraints.NotNull(message = "L'état du projet est obligatoire")
    @jakarta.validation.constraints.Size(min = 2, max = 50, message = "L'état doit comporter entre 2 et 50 caractères")
    private String etat;

    @jakarta.validation.constraints.Size(max = 100, message = "Le port ne doit pas dépasser 100 caractères")
    private String port;
    
    // Champs ajoutés
    private java.time.LocalDate dateDebut;
    private java.time.LocalDate dateFin;
    private Boolean active;
    
    // Sociétés associées (informations complètes)
    private Set<SocieteDTO> societes;

    // Compatibilité: noms des sociétés (lecture/écriture facultative)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<String> societeNoms;
}
