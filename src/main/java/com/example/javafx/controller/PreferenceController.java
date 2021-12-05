package com.example.javafx.controller;
import java.util.prefs.*;

public class PreferenceController {
    private Preferences prefs;

    public PreferenceController(){
        this.prefs = Preferences.userNodeForPackage(this.getClass());
    }

    public void setToken(String token) {
        this.prefs.put("token", token);
    }

    public void clearToken(){
        this.prefs.put("token", "");
    }

    public String getToken() {
        return this.prefs.get("token", "");
    }
}
