package com.example.navire.exception;

public class DechargementNotFoundException extends RuntimeException {
    public DechargementNotFoundException(String message) {
        super(message);
    }

    public DechargementNotFoundException(Long id) {
        super("Dechargement not found with id: " + id);
    }
}
