package com.example.javafx.service.helper;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Klasse für Websocket-Kommunikation.
 */
public class WebSocketConnection extends WebSocketClient {

    private SceneManager sceneManager = null;

    public WebSocketConnection(URI address, SceneManager sceneManager) {
        super(address);
        this.sceneManager = sceneManager;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("connection established...");
        /*
        Ausfuehren des JavaFX UI Render im Mainthread ueber Runlater. vgl Decision Log
        Das muessen wir machen weil der JavaFX Kontext im Mainthread ist. Wenn die Aenderungen in einem
        Childthread machen weis der Mainthread mit dem Kontext nichts darueber. Ueber runLater() fuehren wir die Aufgabe im
        Mainthread aus, sobald er Zeit dafuer hat.
         */
        try {
            javafx.application.Platform.runLater(()->{
                sceneManager.loadGame();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote){
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message){
       System.out.println(message);
    }

    @Override
    public void onError(Exception e){System.out.println(e);}
}