package com.memorio.memorio.exceptions;

/**
 * Exceptionhandling.
 */
public class MemorioRuntimeException extends RuntimeException {
    public MemorioRuntimeException(String message) {
        super(message);
    }

    public MemorioRuntimeException() {
        super();
    }
}