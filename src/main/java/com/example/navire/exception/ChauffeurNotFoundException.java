package com.example.navire.exception;

public class ChauffeurNotFoundException extends RuntimeException {
    public ChauffeurNotFoundException(Long id) {
        super("Chauffeur not found with id: " + id);
    }
}
