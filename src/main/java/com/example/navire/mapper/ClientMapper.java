package com.example.navire.mapper;

import com.example.navire.model.Client;
import com.example.navire.dto.ClientDTO;
import org.mapstruct.*;
import java.util.HashMap;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    @Mapping(target = "quantitesAutoriseesParProjet", ignore = true)
    ClientDTO toDTO(Client client);

    Client toEntity(ClientDTO dto);

    @AfterMapping
    default void mapQuantites(Client client, @MappingTarget ClientDTO dto) {
        Map<Long, Double> quantitesMap = new HashMap<>();
        if (client.getProjetClients() != null) {
            client.getProjetClients().forEach(pc -> {
                if (pc.getProjet() != null) {
                    // Somme des autorisations (compatibilité avec ancien champ quantiteAutorisee)
                    Double total = null;
                    if (pc.getAutorisation() != null && !pc.getAutorisation().isEmpty()) {
                        total = pc.getAutorisation().stream()
                                .filter(a -> a != null && a.getQuantite() != null)
                                .mapToDouble(a -> a.getQuantite())
                                .sum();
                    } else if (pc.getQuantiteAutorisee() != null) {
                        // fallback compatibility
                        total = pc.getQuantiteAutorisee();
                    }
                    if (total != null) {
                        quantitesMap.put(pc.getProjet().getId(), total);
                    }
                }
            });
        }
        dto.setQuantitesAutoriseesParProjet(quantitesMap);
    }
}
