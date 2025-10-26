package com.example.navire.exception;

public class CamionNotFoundException extends RuntimeException {
    public CamionNotFoundException(Long id) {
        super("Camion not found with id: " + id);
    }
    public CamionNotFoundException(String matricule) {
        super("Camion not found with matricule: " + matricule);
    }
}
