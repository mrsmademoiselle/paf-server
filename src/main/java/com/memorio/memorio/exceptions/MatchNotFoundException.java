package com.memorio.memorio.exceptions;


/**
 * Exceptionhandling.
 */
public class MatchNotFoundException extends Exception {
    public MatchNotFoundException() {
        super("Es sind nicht genug Spieler in der Warteschlange, um ein Match zu starten.");
    }
}