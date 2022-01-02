package com.example.javafx.service;

import com.example.javafx.controller.GameController;
import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.WebSocketConnection;
import javafx.scene.Scene;

import java.net.URI;
import java.net.URISyntaxException;

public class GameService implements Runnable {

    SceneManager sceneManager = SceneManager.getInstance();
    WebSocketConnection connection = null;
    Thread thread = null;
    // GameController instanz - wird reingezogen wenn User in der GameView
    GameController gameController = null;

    //TODO:DOKUMENTATION - ENSCHTEIDUNGSLOG PFELGEn
    // Singleton pattern. Dadurch haben wir eine zentrale Verbindung gebunden an einen Service
    private static GameService instance;

    /**
     * Setzen der aktuellen GameController Instanz. Geschieht im GameController selber
     * Damit soll sichergestellt werden, dass immer die aktuellste GameController Instanz im GameService ist
     * @param controller Aktueller GameController
     */
    public void setGameController(GameController controller){
        this.gameController = controller;
    }

    /**
     * Methode zum extrahieren der aktuellen GameController instanz - wird im Websocket verwendet
     * @return Aktuelle GameController instanz
     */
    public GameController getGameController() {
        // Retuniert aktuellen GameController
        return gameController;
    }

    public static GameService getInstance(){
        if(instance == null){
            instance = new GameService();
        }
        return instance;
    }
    private GameService(){}

    public void lookForGame() throws URISyntaxException {
        thread = new Thread(getInstance());
        thread.start();
        javafx.application.Platform.runLater(()->{
            this.sceneManager.loadLobby();
        });
        //sceneManager.loadGame();
    }

    public void stopLookingForGame() {
        stop();
        sceneManager.loadProfile();
    }

    public void stop(){
        if(connection != null){
            try{
                connection.close();
                System.out.print("Connection abgebaut - Thread noch am leben");
            } catch (Exception e){System.out.println(e);}
        }
        if(thread != null) {
            System.out.println("Thread beendet - Connection abgebaut");
            thread.interrupt();
        }
    }

    public void run() {
        try {
            connection = new WebSocketConnection(new URI("ws://127.0.0.1:8888"), sceneManager);
            connection.connect();
        } catch(Exception e){System.out.println(e);}
    }

    //TODO: DOKUMENTATION
    //Testen der verheiratung von GameController und Gameservice sowie Websocket
    public void testActivatedGameController(){
        // Aufrufen der Testmethode im WS, dort wird der GameController aus dem Service gezogen
        connection.activateGameController();
    }
}