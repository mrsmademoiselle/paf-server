package com.example.javafx.service;

import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.WebsocketConnector;
import javafx.application.Platform;

public class GameService {
    SceneManager sceneManager = SceneManager.getInstance();

    public void openLobby() {
        sceneManager.loadLobby();

        WebsocketConnector websocketConnector = WebsocketConnector.getInstance();

        Thread thread = new Thread(() -> {
            // TODO: Absrepchen was wir schicken wollen
            websocketConnector.connect("hallo");
            Platform.runLater(() -> sceneManager.loadGame());
        });
        thread.start();
    }
}