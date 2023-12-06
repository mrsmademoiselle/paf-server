package com.memorio.memorio.valueobjects;

import java.util.Arrays;

public enum MessageKeys {
    REGISTER_QUEUE("LOGIN"),
    DISSOLVE_QUEUE("DISSOLVE"),
    FLIP_CARD("FLIPPED"),
    CANCEL_GAME("CANCEL"),
    HEARTBEAT("HEARTBEAT"),
    JWT("JWT"),
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