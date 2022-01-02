package com.memorio.memorio.websocket;

import java.util.Arrays;

public enum MessageKeys {
    // Der Spieler will sich für die Queue registrieren
    REGISTER_QUEUE("LOGIN"),
    // Der Spieler befindet sich in der Queue und hat noch kein Match gefunden
    DISSOLVE_QUEUE("DISSOLVE"),
    // eine Karte soll umgedreht werden
    FLIP_CARD("FLIPPED"),
    // Der Spieler will das laufende Match abbrechen
    CANCEL_GAME("CANCEL"),
    // wenn kein anderer Key gefunden wurde
    NONE("NONE");

    private final String text;

    MessageKeys(String text) {
        this.text = text;
    }

    public static MessageKeys getEnumForString(String s) {
        return Arrays.stream(MessageKeys.values())
                .filter(messageKey -> messageKey.text.equalsIgnoreCase(s))
                .findFirst()
                .orElse(MessageKeys.NONE);
    }
}