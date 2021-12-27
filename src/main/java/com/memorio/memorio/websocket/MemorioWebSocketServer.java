package com.memorio.memorio.websocket;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class MemorioWebSocketServer extends WebSocketServer {
	
    private final static InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
    private static MemorioWebSocketServer instance = null;

    private Queue<Player> playerQueue = new LinkedList<>();
    private List<Match> matches = new ArrayList<>();

    public void onNewMatch(Match m){
	Player p1 = m.getPlayerOne();
	Player p2 = m.getPlayerTwo();
	p1.setMatch(m);
	p2.setMatch(m);
	matches.add(m);
	System.out.println("-------------------------------------------");
	System.out.println("MATCH FOUND!!!!");
	System.out.println("matching: " + p1.getToken() + " and " + p2.getToken());
	System.out.println("-------------------------------------------");
    }

    public Match matchPlayer() throws MatchNotFoundException {
	if(playerQueue.size() < 2){throw new MatchNotFoundException();}
	Player playerOne = playerQueue.remove();
	Player playerTwo = playerQueue.remove();
	Match match = new Match(playerOne, playerTwo);
	onNewMatch(match);
	return match;
    }

    public Player findPlayerByConnection(WebSocket conn){
	for(Match m : matches){
	    Player p1 = m.getPlayerOne();
	    Player p2 = m.getPlayerTwo();
		if(conn == p1.getConnection()) return p1;
		if(conn == p2.getConnection()) return p2;
	}
	return null;
    }

    public void dissolveMatch(Match m){
	Player p1 = m.getPlayerOne();
	Player p2 = m.getPlayerTwo();
	p1.removeSubscriber(p2);
	p2.removeSubscriber(p1);
	p1.getConnection().close();
	p2.getConnection().close();
	if(matches.contains(m)){matches.remove(m);}
	System.out.println("match dissolved");
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
	Player player = new Player(conn);
	System.out.println("client connected: " + player.getToken());
	playerQueue.add(player);
	try{
	    matchPlayer();
	} catch(MatchNotFoundException e){System.out.println(e);}
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
	Player player = findPlayerByConnection(conn);
	if(player == null) return;
	dissolveMatch(player.getMatch());
	System.out.println("client disconnected: " + player.getToken());
    }
    @Override
    public void onError(WebSocket conn, Exception ex) {System.out.println(ex);}
    @Override
    public void onMessage(WebSocket conn, String message) {
	Player player = findPlayerByConnection(conn);
	if(player == null) return;
	for(int i = 0; i < player.getSubscribers().size(); i++){
	    player.getSubscribers().get(i).getConnection().send(message);
	}
    }
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

class MatchNotFoundException extends Exception {
    public MatchNotFoundException(){super("No match possible");}
}
