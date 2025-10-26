package com.example.navire.exception;

public class DepotNotFoundException extends RuntimeException {
    public DepotNotFoundException(Long id) {
        super("Depot not found with id: " + id);
    }
    public DepotNotFoundException(String nom) {
        super("Depot not found with nom: " + nom);
    }
}
