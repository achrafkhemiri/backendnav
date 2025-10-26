package com.example.navire.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientDTO {
    private Long id;
    private String numero;
    private String nom;
    private String adresse;
    private String mf;
    // Map of projetId to quantiteAutorisee
    private java.util.Map<Long, Double> quantitesAutoriseesParProjet;
}
