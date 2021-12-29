package com.example.javafx.service.helper;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Klasse für Websocket-Kommunikation.
 */
public class WebSocketConnector extends WebSocketClient{

    private static final WebSocketConnector instance = new WebSocketConnector();
    private static final InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
    private WebSocketConnector() {super(address);}
    public static WebSocketConnector getInstance() {return instance;}

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
    public void onError(Exception e){
        System.out.println(e);
    }
}