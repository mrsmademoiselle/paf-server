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


	/**
	 *
	 * @param m
	 */
	public void onNewMatch(Match m){
		// wird aufgerufen wenn ein Match erfolgreich erstellt wurde
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
		System.out.println("MATCH GEFUNDEN!!!!");
		System.out.println("Spieler: " + p1.getToken() + " VS " + p2.getToken());
		System.out.println("-------------------------------------------");
	}


	/**
	 *
	 * @return
	 * @throws MatchNotFoundException
	 */
	public Match matchPlayer() throws MatchNotFoundException {
		// versucht sich zwei Player-Instanzen aus der playerQueue zu holen und diese
		// zu matchen
		if(playerQueue.size() < 2){throw new MatchNotFoundException();}
		Player playerOne = playerQueue.remove();
		Player playerTwo = playerQueue.remove();
		Match match = new Match(playerOne, playerTwo);
		onNewMatch(match);
		return match;
	}

	/**
	 *
	 * @param conn
	 * @return
	 */
	public Player findPlayerByConnection(WebSocket conn){
		// findet einen Player in bestehenden Matches anhand seiner WebSocket
		for(Match m : matches){
			Player p1 = m.getPlayerOne();
			Player p2 = m.getPlayerTwo();
			if(conn == p1.getConnection()) return p1;
			if(conn == p2.getConnection()) return p2;
		}
		return null;
	}

	/**
	 *
	 * @param m
	 */
	public void dissolveMatch(Match m){
		// löst ein Match auf.
		// Momentan werden hier alle subscriber der Player entfernt und alle
		// zum Match gehörenden Connections beendet.
		Player p1 = m.getPlayerOne();
		Player p2 = m.getPlayerTwo();
		p1.removeSubscriber(p2);
		p2.removeSubscriber(p1);
		p1.getConnection().close();
		p2.getConnection().close();
		if(matches.contains(m)){matches.remove(m);}
		System.out.println("match aufgelöst :(");
	}


	/**
	 *
	 * @param conn
	 * @param handshake
	 */
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// wird aufgerufen wenn sich ein neuer Client verbindet
		// erstellt einen Spieler und versucht ein Match zu finden
		Player player = new Player(conn);
		System.out.println("Spieler verbunden: " + player.getToken());
		playerQueue.add(player);
		try{
			matchPlayer();
		} catch(MatchNotFoundException e){System.out.println(e);}
	}

	/**
	 *
	 * @param conn
	 * @param code
	 * @param reason
	 * @param remote
	 */
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// wird aufgerufen wenn ein Client die Verbindung abbricht.
		// wenn der Player in einem Match ist wird dieses aufgelöst.
		Player player = findPlayerByConnection(conn);
		if(player == null) return;
		dissolveMatch(player.getMatch());
		System.out.println("Spieler getrennt: " + player.getToken());
	}


	@Override
	public void onError(WebSocket conn, Exception ex) {System.out.println(ex);}


	/**
	 *
	 * @param conn
	 * @param message
	 */
	@Override
	public void onMessage(WebSocket conn, String message) {
		// wird aufgerufen wenn eine WebSocket eine Nachricht sendet.
		// findet zugehörigen Player und leitet Nachricht an alle Subscriber weiter.
		Player player = findPlayerByConnection(conn);
		if(player == null) return;
		for(int i = 0; i < player.getSubscribers().size(); i++){
			player.getSubscribers().get(i).getConnection().send(message);
		}
	}

	/**
	 * Methode wird aufgerufen wenn der Websocketserver gestartet wird.
	 */
	@Override
	public void onStart() {
		// Gebe aus, sobald der Server hochgefahren wurde
		System.out.println("Beep boop - MemorioWebSocketServer hochgefahren");
	}


	/**
	 * Super Konstruktor zum erstellen des Websocketservers
	 * @param address InetSocketAddress
	 */
	private MemorioWebSocketServer(InetSocketAddress address){
		// rufe Parent Konstruktor mit der InetSocketAddress auf.t
		super(address);
	}

	/**
	 *  Returnen der Websocket Server instanz.
	 *  Wenn es keine Instanz gibt wird eine erzeugt.
	 * @return Websocket Server Instanz
	 */
	public static MemorioWebSocketServer getInstance() {
		if(instance==null){
			instance = new MemorioWebSocketServer(address);
		}
		return instance;
	}
}

/**
 * Exceptionhandling.
 */
class MatchNotFoundException extends Exception {
	public MatchNotFoundException(){super("Kein Match möglich");}
}
