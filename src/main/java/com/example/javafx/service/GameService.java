package com.example.javafx.service;

import com.example.javafx.controller.GameController;
import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.WebSocketConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class GameService implements Runnable {

    SceneManager sceneManager = SceneManager.getInstance();
    WebSocketConnection connection = null;
    Thread thread = null;
    GameController gameController = null;

    //TODO:DOKUMENTATION - ENSCHTEIDUNGSLOG PFELGEn
    // Singleton pattern. Dadurch haben wir eine zentrale Verbindung gebunden an einen Service
    private static GameService instance;

    public void setGameController(GameController controller){
        this.gameController = controller;
    }

    public GameController getGameController() {
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
            } catch (Exception e){System.out.println(e);}
        }
        if(thread != null) {
            thread.interrupt();
        }
    }

    public void run() {
        try {
            connection = new WebSocketConnection(new URI("ws://127.0.0.1:8888"), sceneManager);
            connection.connect();
        } catch(Exception e){System.out.println(e);}
    }
}