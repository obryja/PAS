package org.example.rest.exceptions;

public class PreconditionException extends RuntimeException {
    public PreconditionException(String message) {
        super(message);
    }
}