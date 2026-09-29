package com.example.pipedetection.exception;

/** Thrown when a requested entity (drone, leak, product, contact, etc.) cannot be found. */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
