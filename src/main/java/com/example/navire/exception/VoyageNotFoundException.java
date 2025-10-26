package com.example.navire.exception;

public class VoyageNotFoundException extends RuntimeException {
    public VoyageNotFoundException(Long id) {
        super("Voyage not found with id: " + id);
    }
    public VoyageNotFoundException(String numBonLivraison) {
        super("Voyage not found with numBonLivraison: " + numBonLivraison);
    }
}
