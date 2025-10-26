package com.example.navire.exception;

/**
 * Exception levée lorsqu'une quantité autorisée dépasse la quantité disponible.
 * Cette exception doit retourner un code HTTP 400 (Bad Request), pas 403.
 */
public class QuantiteDepassementException extends RuntimeException {
    public QuantiteDepassementException(String message) {
        super(message);
    }
}
