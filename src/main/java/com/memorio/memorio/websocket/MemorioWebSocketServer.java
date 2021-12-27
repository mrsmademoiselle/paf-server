package com.memorio.memorio.websocket;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


public class MemorioWebSocketServer extends WebSocketServer {
	
    private final static InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
    private static MemorioWebSocketServer instance = null;

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {}
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}
    @Override
    public void onError(WebSocket conn, Exception ex) {}
    @Override
    public void onMessage(WebSocket conn, String message) {}
    @Override
    public void onStart() {System.out.println("hello server");}

    private MemorioWebSocketServer(InetSocketAddress address){super(address);}

    public static MemorioWebSocketServer getInstance() {
	if(instance==null){
	    instance = new MemorioWebSocketServer(address);
	}
	return instance;
    }
}
