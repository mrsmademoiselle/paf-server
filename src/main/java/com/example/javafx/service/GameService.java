package com.example.javafx.service;

import com.example.javafx.service.helper.SceneManager;

public class GameService {
    SceneManager sceneManager = SceneManager.getInstance();

    public void openLobby() {
        sceneManager.loadLobby();
        // todo open thread etc
    }
}