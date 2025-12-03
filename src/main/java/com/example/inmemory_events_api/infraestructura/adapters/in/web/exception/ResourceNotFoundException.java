package com.example.inmemory_events_api.infraestructura.adapters.in.web.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
