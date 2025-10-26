package com.example.navire.exception;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Client not found with id: " + id);
    }
    public ClientNotFoundException(String numero) {
        super("Client not found with numero: " + numero);
    }
}
