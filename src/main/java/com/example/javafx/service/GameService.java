package com.example.javafx.service;

import com.example.javafx.controller.GameController;
import com.example.javafx.service.helper.HttpConnector;
import com.example.javafx.service.helper.MessageKeys;
import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.WebSocketConnection;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

public class GameService implements Runnable {
    // Gameservice der Threadbaren Websocket startet

    // Singleton pattern. Dadurch haben wir eine zentrale Verbindung gebunden an einen Service
    private static GameService instance;
    SceneManager sceneManager = SceneManager.getInstance();
    WebSocketConnection connection = null;
    Thread thread = null;
    // GameController instanz - wird reingezogen wenn User in der GameView
    GameController gameController = null;
    // Gewinner persistieren
    String winner;

    private GameService() {
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    /**
     * Methode zum extrahieren der aktuellen GameController instanz - wird im Websocket verwendet
     *
     * @return Aktuelle GameController instanz
     */
    public GameController getGameController() {
        // Retuniert aktuellen GameController
        return gameController;
    }

    /**
     * Setzen der aktuellen GameController Instanz. Geschieht im GameController selber
     * Damit soll sichergestellt werden, dass immer die aktuellste GameController Instanz im GameService ist
     *
     * @param controller Aktueller GameController
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;
    }

    /**
     * Methode zum extrahieren der Websocketconnection um darueber zu kommunizieren
     *
     * @return Die Websocketconnection
     */
    public WebSocketConnection getWebSocketConnection() {
        return connection;
    }

    /**
     * Suchen nach Spiel, Starten der WS Verbindung
     *
     * @throws URISyntaxException
     */
    public void lookForGame() throws URISyntaxException {
        while (thread != null && thread.isAlive()) {
            System.out.println("Thread lebt noch, Öffnen einer neuen Queue nicht möglich");
        }
        thread = new Thread(getInstance());
        thread.start();
        // Weiterleiten zur Lobbyview
        javafx.application.Platform.runLater(() -> {
            this.sceneManager.loadLobby();
        });
    }

    public void stopLookingForGame() {
        stop();
        sceneManager.loadProfile();
    }

    // Beenden WS Verbindung und beendet Thread
    public void stop() {
        if (connection != null) {
            try {
                // Dissolven der Verbindung wenn die die Verbindung geschlossen wird
                connection.mSend(MessageKeys.DISSOLVE, "");
                connection.close();
                System.out.println("Connection abgebaut - Thread noch am leben");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (thread != null) {
            thread.interrupt();
            System.out.println("Thread beendet - Connection abgebaut");
        }
    }

    // Startet den Thread
    public void run() {
        try {
            connection = new WebSocketConnection(new URI("ws://127.0.0.1:8888"), sceneManager);
            connection.connect();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Testen der verheiratung von GameController und Gameservice sowie Websocket
    public void testActivatedGameController() {
        // Aufrufen der Testmethode im WS, dort wird der GameController aus dem Service gezogen
        connection.activateGameController();
    }

    /**
     * Herausziehen des Usernamens durch den Token
     *
     * @return Username des Users
     */
    public String getUsernamebyToken() {
        // Request an /info Endpunkt
        String username = HttpConnector.get("user/info").getBody();
        // JSONifizieren der Response
        JSONObject jsonObject = new JSONObject(username);
        // Herausziehen des Benutzernamens und returnieren von diesem
        return jsonObject.getString("username");
    }

    public String getWinner() {
        return this.winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}