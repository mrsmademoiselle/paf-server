package com.example.javafx.service.helper;

import java.util.prefs.Preferences;

public class TokenManager {

    private Preferences prefs;
    private static TokenManager instance;

    private TokenManager() {
        this.prefs = Preferences.userNodeForPackage(this.getClass());
        setToken("");
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public void setToken(String token) {
        this.prefs.put("token", token);
    }

    public void clearToken() {
        this.prefs.put("token", "");
    }

    public String getToken() {
        return this.prefs.get("token", "Ich feuer gerade");
    }
}