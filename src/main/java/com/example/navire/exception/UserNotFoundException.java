package com.example.navire.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
    public UserNotFoundException(String mail) {
        super("User not found with mail: " + mail);
    }
}
