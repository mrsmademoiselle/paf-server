package com.memorio.memorio.websocket;

import com.memorio.memorio.config.jwt.JwtTokenUtil;
import com.memorio.memorio.repositories.UserRepository;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;

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

	// Der Konstruktor darf nicht veraendert werden, daher wird der jwtTokenUtil ueber die injection reingezogen
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserRepository userRepository;

	// Adresse und Port
	private final static InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
	// singleton
	private static MemorioWebSocketServer instance = null;

	//  Playerqeue-  hier kommt jeder neue Player rein.
	private Queue<Player> playerQueue = new LinkedList<>();
	// Matchliste - Diese Liste enthält alle erfolgreichen Matches.
	private List<Match> matches = new ArrayList<>();

	// Nachrichten Flags nach denen gesucht werden kann
	private String[] messageFlags = {
			"token",
	};


	/**
	 * Wird aufgerufen wenn ein neues Match gefunden wurde
	 * Teilt den Clients mit das ein Match gefunden wurde und setzt das Match in die Matchliste
	 * @param m Matchobjekt
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
		System.out.println("Spieler: " + p1.getUser().getUsername() + " VS " + p2.getUser().getUsername());
		System.out.println("-------------------------------------------");
	}


	/**
	 * Matchen von zwei Spielern aus der Queue
	 * @return Matchobjekt
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
	 * Finden vom Spieler anhand seiner Verbindung ueber die Matchliste
	 * @param conn Die Socketverbindung nach der gesucht werden soll
	 * @return Gefundener Spieler
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
	 * onOpen Handler, wird aufgerufen wenn ein neuer Client sich verbindet
	 * @param conn
	 * @param handshake
	 */
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// wird aufgerufen wenn sich ein neuer Client verbindet
		// erstellt einen Spieler und versucht ein Match zu finden
		/*
		Player player = new Player(conn);
		System.out.println("Spieler verbunden: " + player.getToken());
		playerQueue.add(player);
		try{
			matchPlayer();
		} catch(MatchNotFoundException e){System.out.println(e);}
		ERSTMAL AUSKOMMENTIERT BIS ZUM 31.12 - ANSONSTEN MACHT DIE ERSTMAL NICHTS WENN DIE VERBINDUNG
		GOEFFNET WIRD AUSSER SIGNALISIEREN DAS SIE OFFEN IST
		 */
		System.out.println("foobar");
	}

	/**
	 * Closehandler, wird aufgerufen wenn Spieler getrennt wird
	 * @param conn Websocketverbindung
	 * @param code Exitcode - wird aktuell nicht verwendet
	 * @param reason Wird nicht verwendet
	 * @param remote Wird nicht verwendet
	 */
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		// wird aufgerufen wenn ein Client die Verbindung abbricht.
		// wenn der Player in einem Match ist wird dieses aufgelöst.
		Player player = findPlayerByConnection(conn);
		if(player == null) return;
		dissolveMatch(player.getMatch());
		System.out.println("Spieler getrennt: " + player.getUser().getUsername());
	}


	/**
	 * Errorhandler
	 * @param conn Socketverbindung
	 * @param ex Exception
	 */
	@Override
	public void onError(WebSocket conn, Exception ex) {System.out.println(ex);}


	/**
	 * Nachrichtenhandler, sucht Spieler anhand von Websocket und informiert Subscriber
	 * @param conn Websocketverbindung ueber die ein User gefunden werden soll
	 * @param message Nachricht die weitergeleitet werden soll
	 */
	@Override
	public void onMessage(WebSocket conn, String message) {
		// wird aufgerufen wenn eine WebSocket eine Nachricht sendet.
		// findet zugehörigen Player und leitet Nachricht an alle Subscriber weiter.

		/*
		Verarbeiten der Nachricht, durchsuchen nach Flags und entsprechendes Handling der Nachricht
		 Wenn die Nachricht ein entsprechendes Flag beeinhaltet bricht die Schleife
		 Durch die Position des Pointers koennen wir im Switch/Case spaeter entsprechend auf die Nachricht reagieren
		 */
		int pointer;
		for (pointer = 0; pointer < messageFlags.length; pointer++){
			if(message.contains(messageFlags[pointer])) break;
		}
		// Flaghandling - Hier weitere Messageflags hinzufuegen
		switch (pointer){
			//flag: token
			case 0:{
				// wird aufgerufen wenn ein Client in der Nachricht das 'token' flag gesetzt hat
				// erstellt einen Spieler und versucht ein Match zu finden
				String jwt = message.substring(message.lastIndexOf(":") + 1);
				conn.send("Tokensuchmoodus aktiviert, suche Spieler");
				Player player = new Player(conn, userRepository.findByUsername(jwtTokenUtil.getUsernameFromToken(jwt)), jwt);
				System.out.println("Spieler verbunden: " + player.getUser().getUsername());
				playerQueue.add(player);
				try{
					matchPlayer();
					break;
				} catch(MatchNotFoundException e){System.out.println(e);
					break;
				}
			}
			// Wenn kein Flag gesetzt, sende Nachricht an alle Subscriber
			default:{
				Player player = findPlayerByConnection(conn);
				if(player == null) return;
				for(int i = 0; i < player.getSubscribers().size(); i++){
					// Ziehe vom Spieler mit der gefundenen Websocket alle Subscriber,
					// deren Verbindungen und sende Nachricht
					player.getSubscribers().get(i).getConnection().send(message);
				}
			}
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
