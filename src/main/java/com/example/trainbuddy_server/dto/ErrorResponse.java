package com.example.trainbuddy_server.dto;

import java.util.List;
import java.util.Map;

/**
 * Standard error response for all API exceptions.
 */
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private List<Map<String,String>> details;

    public ErrorResponse() { }

    public ErrorResponse(int status, String error, String message) {
        this(status, error, message, null);
    }

    public ErrorResponse(int status, String error, String message, List<Map<String,String>> details) {
        this.status  = status;
        this.error   = error;
        this.message = message;
        this.details = details;
    }

    // getters & setters

    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public List<Map<String, String>> getDetails() {
        return details;
    }
    public void setDetails(List<Map<String, String>> details) {
        this.details = details;
    }
}
