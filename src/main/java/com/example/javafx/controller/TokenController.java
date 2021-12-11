package com.example.javafx.controller;
import java.util.prefs.*;

public class TokenController {

    private Preferences prefs;
    private static TokenController instance;

    private TokenController(){
        this.prefs = Preferences.userNodeForPackage(this.getClass());
        setToken("");
    }

    public static TokenController getInstance() {
        if (instance == null) {
            instance = new TokenController();
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
