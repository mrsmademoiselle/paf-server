package com.example.javafx.controller;
import java.util.prefs.*;

public class PreferenceController {

    private Preferences prefs;
    private static PreferenceController instance;

    private PreferenceController(){
        this.prefs = Preferences.userNodeForPackage(this.getClass());
        setToken("");
    }

    public static PreferenceController getInstance() {
        if (instance == null) {
            instance = new PreferenceController();
        }
        return instance;
    }

    public void setToken(String token) {
        this.prefs.put("token", token);
    }

    public void clearToken(){
        this.prefs.put("token", "");
    }

    public String getToken() {
        return this.prefs.get("token", "Ich feuer gerade");
    }
}
