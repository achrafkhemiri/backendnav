package com.example.navire.exception;

public class ProjetNotFoundException extends RuntimeException {
    public ProjetNotFoundException(Long id) {
        super("Projet not found with id: " + id);
    }
}
