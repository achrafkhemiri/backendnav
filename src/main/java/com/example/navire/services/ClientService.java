package com.example.navire.services;

import com.example.navire.dto.ClientDTO;
import com.example.navire.exception.ClientNotFoundException;
import com.example.navire.mapper.ClientMapper;
import com.example.navire.model.Client;
import com.example.navire.repository.ClientRepository;
import com.example.navire.repository.ProjetClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProjetClientRepository projetClientRepository;
    @Autowired
    private ClientMapper clientMapper;

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return clientMapper.toDTO(client);
    }

    @Transactional
    public ClientDTO createClient(ClientDTO dto) {
        if (clientRepository.existsByNumero(dto.getNumero())) {
            throw new IllegalArgumentException("Numero already exists");
        }
        Client client = clientMapper.toEntity(dto);
        return clientMapper.toDTO(clientRepository.save(client));
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientDTO dto) {
    Client client = clientRepository.findById(id)
        .orElseThrow(() -> new ClientNotFoundException(id));
    client.setNumero(dto.getNumero());
    client.setNom(dto.getNom());
    client.setAdresse(dto.getAdresse());
    client.setMf(dto.getMf());
    // QuantiteAutorisee is managed via ProjetClient, not directly here
    return clientMapper.toDTO(clientRepository.save(client));
    }

    public List<ClientDTO> getClientsByProjetId(Long projetId) {
        List<Client> clients = projetClientRepository.findClientsByProjetId(projetId);
        // Pour chaque client, filtrer les quantités autorisées pour le projet demandé
        return clients.stream().map(client -> {
            ClientDTO dto = clientMapper.toDTO(client);
            if (dto.getQuantitesAutoriseesParProjet() != null) {
                // Ne garder que la quantité autorisée pour ce projet
                Double quantite = dto.getQuantitesAutoriseesParProjet().get(projetId);
                java.util.Map<Long, Double> map = new java.util.HashMap<>();
                if (quantite != null) {
                    map.put(projetId, quantite);
                }
                dto.setQuantitesAutoriseesParProjet(map);
            }
            return dto;
        }).collect(java.util.stream.Collectors.toList());
    }

    @Transactional
    public void deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new ClientNotFoundException(id);
        }
        clientRepository.deleteById(id);
    }
}
