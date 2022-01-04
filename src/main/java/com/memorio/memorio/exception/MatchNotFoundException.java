package com.memorio.memorio.exception;


/**
 * Exceptionhandling.
 */
public class MatchNotFoundException extends Exception {
    public MatchNotFoundException() {
        super("Kein Game möglich");
    }
}