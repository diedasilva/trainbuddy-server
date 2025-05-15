package com.example.trainbuddy_server.dto;

import java.time.Instant;

public class ErrorResponse {

    private final int status;
    private final String error;
    private final String message;
    private final Instant timestamp = Instant.now();

    public ErrorResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

    // Getters
    public int getStatus() {
        return status;
    }
    public String getError() {
        return error;
    }
    public String getMessage() {
        return message;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
}
