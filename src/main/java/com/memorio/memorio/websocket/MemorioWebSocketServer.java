package com.memorio.memorio.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.memorio.memorio.config.jwt.JwtTokenUtil;
import com.memorio.memorio.entities.*;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.services.BeanUtil;
import com.memorio.memorio.services.GameHandler;
import com.memorio.memorio.services.MemorioJsonMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.*;


/**
 * WebSocketServer: https://github.com/TooTallNate/Java-WebSocket
 * Match = Verknüpfung zweier Player-Instanzen.
 */

public class MemorioWebSocketServer extends WebSocketServer {

    public static final GameHandler gameHandler = new GameHandler();
    // Adresse und Port
    private final static InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8888);
    private static MemorioWebSocketServer instance = null;
    // hier kommt jeder neue Player rein.
    private Queue<Player> playerQueue = new LinkedList<>();
    // Diese Liste enthält alle erfolgreichen Matches.
    private List<Match> matches = new ArrayList<>();

    // Nachrichten Flags nach denen gesucht werden kann
    private String[] messageFlags = {
            "token",
    };
    // >> Das wird so nicht funktionieren. Autowired darf man nur in Spring-Components verwenden, hier verwenden wir allerdings vanilla Java.
    // Der Konstruktor darf nicht verändert werden, daher wird der jwtTokenUtil über die injection reingezogen


    /**
     * Super Konstruktor zum Erstellen des Websocketservers
     */
    private MemorioWebSocketServer(InetSocketAddress address) {
        super(address);
    }

    /**
     * Returnen der Websocket Server Instanz.
     * Wenn es keine Instanz gibt, wird eine erzeugt.
     */
    public static MemorioWebSocketServer getInstance() {
        if (instance == null) {
            instance = new MemorioWebSocketServer(address);
        }
        return instance;
    }

    /**
     * Nachrichtenhandler, sucht Spieler anhand von Websocket und informiert Subscriber
     *
     * @param conn    Websocketverbindung ueber die ein User gefunden werden soll
     * @param message Nachricht die weitergeleitet werden soll
     */
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            Map<String, String> jsonMap = MemorioJsonMapper.getMapFromString(message);
            // exception: falsche größe
            if (jsonMap.keySet().size() != 1)
                throw new RuntimeException("Falsche Größe von Json-Keyset: " + jsonMap.keySet().size());

            handleMessage(conn, jsonMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * onOpen Handler, wird aufgerufen wenn ein neuer Client sich verbindet
     *
     * @param conn
     * @param handshake
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("Verbindung geöffnet");
    }

    /**
     * Closehandler, wird aufgerufen wenn Spieler getrennt wird
     *
     * @param conn   Websocketverbindung
     * @param code   Exitcode - wird aktuell nicht verwendet
     * @param reason Wird nicht verwendet
     * @param remote Wird nicht verwendet
     */
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Verbindung wird geschlossen.........");
        // wird aufgerufen wenn ein Client die Verbindung abbricht.
        // wenn der Player in einem Game ist wird dieses aufgelöst.
        Player player = findPlayerByMatchConnection(conn);

        if (player == null) return;
        dissolveMatch(player.getMatch());
        System.out.println("Spieler getrennt: " + player.getUser().getUsername());
    }

    /**
     * Errorhandler
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.out.println(ex);
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
     * Matchen von zwei Spielern aus der Queue
     *
     * @throws MatchNotFoundException
     */
    public Match matchPlayer() throws MatchNotFoundException {
        // versucht sich zwei Player-Instanzen aus der playerQueue zu holen und diese
        // zu matchen
        if (playerQueue.size() < 2) {
            throw new MatchNotFoundException();
        }
        Player playerOne = playerQueue.remove();
        Player playerTwo = playerQueue.remove();
        Match match = new Match(playerOne, playerTwo);
        onNewMatch(match);
        return match;
    }

    /**
     * Wird aufgerufen wenn ein neues Game gefunden wurde
     * Teilt den Clients mit, dass ein Game gefunden wurde und setzt das Game in die Matchliste
     */
    public void onNewMatch(Match match) {
        // wird aufgerufen wenn ein Game erfolgreich erstellt wurde
        // Player extrahieren
        Player p1 = match.getPlayerOne();
        Player p2 = match.getPlayerTwo();

        // zueinander subscriben
        p1.addSubscriber(p2);
        p2.addSubscriber(p1);
        // Match initialisieren
        p1.setMatch(match);
        p2.setMatch(match);
        // zu matches hinzufügen
        matches.add(match);

        System.out.println("-------------------------------------------");
        System.out.println("MATCH GEFUNDEN!!!!");
        System.out.println("Spieler: " + p1.getUser().getUsername() + " VS " + p2.getUser().getUsername());
        System.out.println("-------------------------------------------");
    }

    /**
     * Finden vom Spieler anhand seiner Verbindung über die Matchliste
     *
     * @param websocketConnection Die Socketverbindung, nach der gesucht werden soll
     * @return Gefundener Spieler oder null
     */
    public Player findPlayerByMatchConnection(WebSocket websocketConnection) {
        // findet einen Player in bestehenden Matches anhand seiner WebSocket
        for (Match match : matches) {
            Player p1 = match.getPlayerOne();
            Player p2 = match.getPlayerTwo();
            if (websocketConnection == p1.getWebsocketConnection()) return p1;
            if (websocketConnection == p2.getWebsocketConnection()) return p2;
        }
        return null;
    }

    /**
     * Aufloesen des Matches. Gegenseitiges entfernen aller Subscriber und Spieler im Game
     *
     * @param match Game das aufgeloest werden soll
     */
    public void dissolveMatch(Match match) {
        // löst ein Game auf.
        // Momentan werden hier alle subscriber der Player entfernt und alle
        // zum Game gehörenden Connections beendet.
        Player p1 = match.getPlayerOne();
        Player p2 = match.getPlayerTwo();
        /* Zu prüfen ob das notwendig ist
        p1.removeSubscriber(p2);
        p2.removeSubscriber(p1);
         */
        p1.getWebsocketConnection().close();
        p2.getWebsocketConnection().close();
        match.removeAllPlayers();

        matches.remove(match);
        System.out.println("match aufgelöst :(");
    }

    private void handleMessage(WebSocket conn, Map<String, String> jsonMap) {
        // .get() müssen wir hier nicht double-checken, weil wir das vor dem Methodenaufruf abfangen
        String keyString = jsonMap.keySet().stream().findFirst().get();
        MessageKeys messageKey = MessageKeys.getEnumForString(keyString);

        switch (messageKey) {
            case LOGIN:
                String jwt_login = jsonMap.get(keyString);
                System.out.println("login-token erhalten: " + jwt_login);

                verifyAndCreateConnection(conn, jwt_login);
                break;
            case DISSOLVE:
                Player player_dissolve = findPlayerInQueueByConnection(conn);
                if (player_dissolve == null) return;
                playerQueue.remove(player_dissolve);
                break;
            case FLIP_CARD:
                String cardId = jsonMap.get(keyString);
                gameHandler.flipCard(cardId);
                sendGameToAllClientsOfConnection(conn);
                break;
            case CANCEL:
                String reason = jsonMap.get(keyString);
                sendEndscoreToClientsOfConnection(conn);
                break;
            default:
                // Ziehe vom Spieler mit der gefundenen Websocket alle Subscriber,
                // deren Verbindungen und sende Nachricht
                String message = jsonMap.get(keyString);
                Player player_default = findPlayerByMatchConnection(conn);
                if (player_default == null) return;

                player_default.getSubscriber().getWebsocketConnection().send(message);
                break;
        }
    }

    private void sendEndscoreToClientsOfConnection(WebSocket conn) {
        try {
            Player player = findPlayerByMatchConnection(conn);
            if (player == null) return;

            // erstelle Endscore-Objekt aus Game-Objekt
            Game game = gameHandler.getGame();
            Endscore endscore = new Endscore(game.getUserScores());
            String message = MemorioJsonMapper.getStringFromObject(endscore);

            player.getSubscriber().getWebsocketConnection().send(message);
            System.out.println("sent endscore to " + player.getSubscriber().getUser().getUsername());

            player.getWebsocketConnection().send(message);
            System.out.println("sent endscore to original User" + player.getUser().getUsername());

            dissolveMatch(player.getMatch());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private Player findPlayerInQueueByConnection(WebSocket connection) {
        return playerQueue.stream()
                .filter(player -> player.getWebsocketConnection() == connection)
                .findFirst().orElse(null);
    }

    private void sendGameToAllClientsOfConnection(WebSocket conn) {
        try {
            Game game = gameHandler.getGame();
            String message = MemorioJsonMapper.getStringFromObject(game);

            Player player = findPlayerByMatchConnection(conn);
            if (player == null) return;

            player.getSubscriber().getWebsocketConnection().send(message);
            System.out.println("sent game to " + player.getSubscriber().getUser().getUsername());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private void verifyAndCreateConnection(WebSocket conn, String jwt) {
        Player player = getPlayerForJwt(conn, jwt);
        if (player == null) return;
        System.out.println("Spieler verbunden: " + player.getUser().getUsername());

        if (!playerQueue.contains(player)) {
            playerQueue.add(player);
        }

        try {
            matchPlayer();
            Game game = new Game(player.getUser(), new Board());
            String message = MemorioJsonMapper.getStringFromObject(game);
            player.getSubscriber().getWebsocketConnection().send(message);
            System.out.println("sent game to " + player.getSubscriber().getUser().getUsername());

            gameHandler.setGame(game);
            conn.send(message);
            System.out.println("sent game to original player " + player.getUser().getUsername());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Player getPlayerForJwt(WebSocket conn, String jwt) {
        JwtTokenUtil jwtTokenUtil = BeanUtil.getBean(JwtTokenUtil.class);
        UserRepository userRepository = BeanUtil.getBean(UserRepository.class);

        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(jwt);
        System.out.println("username: " + usernameFromToken);
        Optional<User> userForToken = userRepository.findByUsername(usernameFromToken);
        System.out.println("user: " + userForToken);
        // todo exception werfen oder so
        if (userForToken.isEmpty()) return null;

        return new Player(conn, userForToken.get(), jwt);
    }

}

/**
 * Exceptionhandling.
 */
class MatchNotFoundException extends Exception {
    public MatchNotFoundException() {
        super("Kein Game möglich");
    }
}