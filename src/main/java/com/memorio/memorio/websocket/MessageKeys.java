package com.memorio.memorio.websocket;

import java.util.Arrays;

public enum MessageKeys {
    LOGIN("LOGIN"),
    FLIP_CARD("FLIP_CARD"),
    CANCEL("CANCEL"),
    DISSOLVE("DISSOLVE");

    private String text;

    MessageKeys(String text) {
        this.text = text;
    }

    public static MessageKeys getEnumForString(String s) {
        return Arrays.stream(MessageKeys.values())
                .filter(e -> e.toString().equalsIgnoreCase(s))
                .findFirst()
                .orElse(null);
    }
}