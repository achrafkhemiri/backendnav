package com.example.navire.controller;
import com.example.navire.dto.ClientDTO;
import com.example.navire.services.ClientService;
import com.example.navire.exception.ClientNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<ClientDTO> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @GetMapping("/projet/{projetId}")
    public List<ClientDTO> getClientsByProjet(@PathVariable Long projetId) {
        return clientService.getClientsByProjetId(projetId);
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO dto) {
        ClientDTO created = clientService.createClient(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO dto) {
        ClientDTO updated = clientService.updateClient(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ClientNotFoundException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleException(Exception ex) {
        if (ex instanceof ClientNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
