package com.memorio.memorio.exceptions;


/**
 * Exceptionhandling.
 */
public class MatchNotFoundException extends Exception {
    public MatchNotFoundException() {
        super("Kein Game möglich");
    }
}