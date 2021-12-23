package com.example.javafx.service;

import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.WebsocketConnector;
import javafx.application.Platform;

public class GameService {
    SceneManager sceneManager = SceneManager.getInstance();
    WebsocketConnector websocketConnector = WebsocketConnector.getInstance();
    Thread backgroundThread = null;

    public void openLobby() {
        sceneManager.loadLobby();

        backgroundThread = new Thread(() -> {
            // TODO: Absrepchen was wir schicken wollen
            boolean foundSuccessfully = websocketConnector.connect("hallo");

            // TODO: später das ResponseObjekt irgendwie hier zwischenspeichern, wenn notwendig

            // Diese Zeile wird nach Beendigung des Threads vom Main-Thread der Anwendung ausgeführt
            Platform.runLater(() -> {
                if (foundSuccessfully) sceneManager.loadGame();
            });
        });
        backgroundThread.start();
    }

    public void stopQueue() {
        if (backgroundThread != null) {
            // Diese Abbruchbedingung klappt noch nicht ganz
            backgroundThread.interrupt();
        }
        sceneManager.loadProfile();
    }
}