package com.example.javafx.service;

import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.WebSocketConnector;
import javafx.application.Platform;

public class GameService {
    SceneManager sceneManager = SceneManager.getInstance();
    WebSocketConnector webSocketConnector = WebSocketConnector.getInstance();
    Thread backgroundThread = null;

    public void openLobby() {
        sceneManager.loadLobby();

        /* Der Thread wurde in diese Datei ausgelagert, damit wir die Behandlung des Ergebnisses (in diesem Fall
         * das Finden eines zweiten Users) hier behandeln können. Wäre stattdessen die Methode im SocketConnector
         * gethreaded, könnten wir hier nicht angeben, was passieren soll, wenn das Ergebnis gefunden wird, weil die
         * in Threads definierten Methoden anonyme Funktionen sind und deren Ergebnis nicht (einfach) zurückgegeben
         * werden kann. Callbacks in Java gibt es zwar, aber die sind sehr umständlich und vom Boilerplatecode nicht
         * vergleichbar mit JavaScript... Leider.
         * Durch die Auslagerung der Threaderstellung wird Aufgabenkapselung erreicht und später doppelter Code
         * vermieden, wenn wir den SocketConnector für andere Dinge verwenden wollen.
         */
        backgroundThread = new Thread(() -> {
            // TODO: Absprechen was wir schicken wollen
            try {
                webSocketConnector.connect();
            } catch(Exception e){System.out.println(e);}

            // TODO: später das ResponseObjekt irgendwie hier zwischenspeichern oder bearbeiten, wenn notwendig

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