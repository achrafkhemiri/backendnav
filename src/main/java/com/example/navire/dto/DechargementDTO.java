package com.example.navire.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DechargementDTO {

    private Long id;
    private Long chargementId;
    private String numTicket;
    private String numBonLivraison;
    private Double poidCamionVide;
    private Double poidComplet;
    private Long clientId;
    private Long depotId;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateDechargement;
    
    // Données du chargement
    private Long camionId;
    private Long chauffeurId;
    private String societe;
    private Long projetId;
    private String produit;
    private String navire;
    private String port;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateChargement;
    // Autorisation code selected for this dechargement (optional)
    private String autorisationCode;
}
