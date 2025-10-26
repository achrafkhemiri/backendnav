package com.example.navire.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjetClientDTO {
    private Long id;
    private Long projetId;
    private Long clientId;
    private java.util.Set<AutorisationDTO> autorisation;
    // Compatibilité ascendante : si le front envoie encore une quantité unique
    private Double quantiteAutorisee;
}
