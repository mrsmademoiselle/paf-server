package com.example.javafx.service.helper;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Klasse für Websocket-Kommunikation.
 */
public class WebSocketConnection extends WebSocketClient {

    public WebSocketConnection(URI address) {super(address);}

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("connection established...");
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