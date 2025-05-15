// 401 – identifiants invalides
package com.example.trainbuddy_server.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid username or password");
    }
}
