package com.example.navire.exception;

public class ChargementNotFoundException extends RuntimeException {
    public ChargementNotFoundException(String message) {
        super(message);
    }

    public ChargementNotFoundException(Long id) {
        super("Chargement not found with id: " + id);
    }
}
