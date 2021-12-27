package com.memorio.memorio.websocket;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class MemorioWebSocketServer extends WebSocketServer {

    /*
     * WebSocketServer: https://github.com/TooTallNate/Java-WebSocket
     *
     *  Match bezieht sich in diesem Fall auf die Verknüpfung zweier
     *  Player-Instanzen. 
     */ 
	
    // ADDRESSE
    private final static InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
    // singleton
    private static MemorioWebSocketServer instance = null;

    // hier kommt jeder neue Player rein.
    private Queue<Player> playerQueue = new LinkedList<>();
    // Diese Liste enthält alle erfolgreichen Matches.
    private List<Match> matches = new ArrayList<>();


    // wird aufgerufen wenn ein Match erfolgreich erstellt wurde
    public void onNewMatch(Match m){
	// Player extrahieren
	Player p1 = m.getPlayerOne();
	Player p2 = m.getPlayerTwo();
	// zueinander subscriben
	p1.addSubscriber(p2);
	p2.addSubscriber(p1);
	// Match initialisieren
	p1.setMatch(m);
	p2.setMatch(m);
	// zu matches hinzufügen
	matches.add(m);
	System.out.println("-------------------------------------------");
	System.out.println("MATCH FOUND!!!!");
	System.out.println("matching: " + p1.getToken() + " and " + p2.getToken());
	System.out.println("-------------------------------------------");
    }

    // versucht sich zwei Player-Instanzen aus der playerQueue zu holen und diese
    // zu matchen
    public Match matchPlayer() throws MatchNotFoundException {
	if(playerQueue.size() < 2){throw new MatchNotFoundException();}
	Player playerOne = playerQueue.remove();
	Player playerTwo = playerQueue.remove();
	Match match = new Match(playerOne, playerTwo);
	onNewMatch(match);
	return match;
    }

    // findet einen Player in bestehenden Matches anhand seiner WebSocket
    public Player findPlayerByConnection(WebSocket conn){
	for(Match m : matches){
	    Player p1 = m.getPlayerOne();
	    Player p2 = m.getPlayerTwo();
		if(conn == p1.getConnection()) return p1;
		if(conn == p2.getConnection()) return p2;
	}
	return null;
    }

    // löst ein Match auf. 
    // Momentan werden hier alle subscriber der Player entfernt und alle
    // zum Match gehörenden Connections beendet.
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

    // wird aufgerufen wenn sich ein neuer Client verbindet
    // erstellt einen Spieler und versucht ein Match zu finden
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
	Player player = new Player(conn);
	System.out.println("client connected: " + player.getToken());
	playerQueue.add(player);
	try{
	    matchPlayer();
	} catch(MatchNotFoundException e){System.out.println(e);}
    }

    // wird aufgerufen wenn ein Client die Verbindung abbricht.
    // wenn der Player in einem Match ist wird dieses aufgelöst.
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
	Player player = findPlayerByConnection(conn);
	if(player == null) return;
	dissolveMatch(player.getMatch());
	System.out.println("client disconnected: " + player.getToken());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {System.out.println(ex);}

    // wird aufgerufen wenn eine WebSocket eine Nachricht sendet.
    // findet zugehörigen Player und leitet Nachricht an alle Subscriber weiter.
    @Override
    public void onMessage(WebSocket conn, String message) {
	Player player = findPlayerByConnection(conn);
	if(player == null) return;
	for(int i = 0; i < player.getSubscribers().size(); i++){
	    player.getSubscribers().get(i).getConnection().send(message);
	}
    }

    @Override
    public void onStart() {System.out.println("hello MemorioWebSocketServer");}

    // rufe Parent Konstruktor mit der InetSocketAddress auf.t
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
