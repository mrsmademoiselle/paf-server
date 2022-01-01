package com.memorio.memorio.websocket;

import java.util.Arrays;

public enum MessageKeys {
    // Der Spieler will sich für die Websocketverbindung authentifizieren
    LOGIN("LOGIN"),
    // Der Spieler befindet sich in der Queue und hat noch kein Match gefunden
    DISSOLVE("DISSOLVE"),
    // eine Karte soll umgedreht werden
    FLIP_CARD("FLIP_CARD"),
    // Der Spieler will das laufende Match abbrechen
    CANCEL("CANCEL"),
    // wenn kein anderer Key gefunden wurde
    NONE("NONE");

    private String text;

    MessageKeys(String text) {
        this.text = text;
    }

    public static MessageKeys getEnumForString(String s) {
        return Arrays.stream(MessageKeys.values())
                .filter(e -> e.toString().equalsIgnoreCase(s))
                .findFirst()
                .orElse(MessageKeys.NONE);
    }
}