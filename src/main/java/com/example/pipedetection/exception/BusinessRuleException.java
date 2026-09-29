package com.example.pipedetection.exception;

/**
 * Thrown when a business rule is violated, e.g. dispatching a drone that
 * is not available, or consuming more sealant than is in stock.
 */
public class BusinessRuleException extends RuntimeException {
    public BusinessRuleException(String message) {
        super(message);
    }
}
