package com.example.javafx.service.helper;

import java.util.prefs.Preferences;

public class TokenManager {

    private static TokenManager instance;
    private final Preferences prefs;

    private TokenManager() {
        /*
        Wenn man zwei JavaFX-Clients mit IntelliJ startet, wird this.prefs für beide Clients genau denselben absoluten Pfad haben, weil der Kontext derselbe ist.
        Das führt zwangsläufig dazu, dass beide Clients denselben JWT haben.
        Es ist deshalb (aktuell!) nicht möglich, das Spiel mit zwei JavaFX-Clients zu spielen, sondern nur Cross-Platform.
         */
        this.prefs = Preferences.userNodeForPackage(this.getClass());
        setToken("");
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    public void clearToken() {
        this.prefs.put("token", "");
    }

    public String getToken() {
        return this.prefs.get("token", "Ich feuer gerade");
    }

    public void setToken(String token) {
        this.prefs.put("token", token);
    }
}