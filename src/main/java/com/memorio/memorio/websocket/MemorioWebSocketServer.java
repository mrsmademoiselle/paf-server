package com.memorio.memorio.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.memorio.memorio.config.jwt.JwtTokenUtil;
import com.memorio.memorio.entities.*;
import com.memorio.memorio.exception.MatchNotFoundException;
import com.memorio.memorio.exception.MemorioRuntimeException;
import com.memorio.memorio.repositories.GameRepository;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.services.BeanUtil;
import com.memorio.memorio.services.GameHandler;
import com.memorio.memorio.services.MemorioJsonMapper;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Logger logger = LoggerFactory.getLogger(MemorioWebSocketServer.class);
    List<String> lostConnectionJwt = new ArrayList<>();
    // hier kommt jeder neue Player rein.
    private Queue<Player> playerQueue = new LinkedList<>();
    // Diese Liste enthält alle erfolgreichen Matches.
    private List<Match> matches = new ArrayList<>();

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

            if (jsonMap.keySet().size() != 2) {
                throw new MemorioRuntimeException("JSON-Keyset muss aus 2 Elementen bestehen.");
            }

            handleMessage(conn, jsonMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
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
        // wird aufgerufen wenn ein Client die Verbindung abbricht.
        // wenn der Player in einem Game ist wird dieses aufgelöst.
        Player player = findPlayerByMatchConnection(conn);
        dissolveMatch(player.getMatch());
        logger.info("Verbindung geschlossen: " + player.getUser().getUsername());
    }

    /**
     * Errorhandler
     */
    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.error(ex.getMessage());
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
    public void matchPlayer() throws MatchNotFoundException {
        // versucht sich zwei Player-Instanzen aus der playerQueue zu holen und diese
        // zu matchen
        if (playerQueue.size() < 2) {
            throw new MatchNotFoundException();
        }
        Player playerOne = playerQueue.remove();
        Player playerTwo = playerQueue.remove();
        Match match = new Match(playerOne, playerTwo);
        onNewMatch(match);

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

        logger.info("Match gefunden:::: " + p1.getUser().getUsername() + " VS " + p2.getUser().getUsername());
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
        throw new MemorioRuntimeException("Es konnte kein Spieler mit der Websocketverbindung gefunden werden.");
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

        p1.getWebsocketConnection().close();
        p2.getWebsocketConnection().close();
        match.removeAllPlayers();

        matches.remove(match);
        logger.info("Spieler-Match wurde aufgelöst.");
    }

    private void handleMessage(WebSocket conn, Map<String, String> jsonMap) {
        List<String> keySetList = new ArrayList<>(jsonMap.keySet());

        // Wir holen uns unser ActionFlag sowie das JWT Token aus dem ehemaligen JSON Objekt vom Client
        String actionFlag = keySetList.get(0); // .get(0) müssen wir hier nicht double-checken, weil wir das vor dem Methodenaufruf abfangen
        MessageKeys messageKey = MessageKeys.getEnumForString(actionFlag);

        if (!jsonMap.containsKey(MessageKeys.JWT.toString())) {
            throw new MemorioRuntimeException("Es wurde kein JWT Token mitgeschickt");
        }
        String jwt = jsonMap.get(MessageKeys.JWT.toString());

        switch (messageKey) {
            case REGISTER_QUEUE:
                verifyAndCreateConnection(conn, jwt);
                break;
            case DISSOLVE_QUEUE:
                // prüfen, ob sich die RemoteAddress von diesem Client geändert hat oder nicht
                conn = updateConnectionIfChanged(conn, jwt);

                Player player_dissolve = findPlayerInQueueByConnection(conn);
                if (player_dissolve == null)
                    throw new MemorioRuntimeException("Der zu entfernende Spieler wurde nicht in der Queue gefunden.");

                playerQueue.remove(player_dissolve);
                logger.info("Der Spieler wurde erfolgreich aus der Queue entfernt.");

                break;
            case FLIP_CARD:
                // prüfen, ob sich die RemoteAddress von diesem Client geändert hat oder nicht
                conn = updateConnectionIfChanged(conn, jwt);

                // Prüfen, ob der User überhaupt ziehen darf
                Player player = findPlayerByMatchConnection(conn);
                if (!gameHandler.getGame().getCurrentTurn().equals(player.getUser()))
                    throw new MemorioRuntimeException("Dieser Spieler ist gerade nicht am Zug.");

                // Flip-Logik
                String cardId = jsonMap.get(actionFlag);
                boolean hasAnyUnflippedCardsLeft = gameHandler.flipCard(cardId);

                // Response an Client senden, Game oder Endscore
                if (hasAnyUnflippedCardsLeft) {
                    sendGameToAllClientsOfConnection(conn);
                } else {
                    sendEndscoreToClientsOfConnection(conn);
                }
                break;
            case CANCEL_GAME:
                // prüfen, ob sich die RemoteAddress von diesem Client geändert hat oder nicht
                conn = updateConnectionIfChanged(conn, jwt);

                String reason = jsonMap.get(actionFlag);
                sendEndscoreToClientsOfConnection(conn);
                break;
            case HEARTBEAT:
                // prüfen, ob sich die RemoteAddress von diesem Client geändert hat oder nicht
                conn = updateConnectionIfChanged(conn, jwt);
                // ???
                break;
            default:
                // prüfen, ob sich die RemoteAddress von diesem Client geändert hat oder nicht
                conn = updateConnectionIfChanged(conn, jwt);

                // Ziehe vom Spieler mit der gefundenen Websocket alle Subscriber,
                // deren Verbindungen und sende Nachricht
                String message = jsonMap.get(actionFlag);
                Player player_default = findPlayerByMatchConnection(conn);

                player_default.getSubscriber().getWebsocketConnection().send(message);
                break;
        }
    }

    /**
     * Prüft, ob sich die Adresse zu einem Client geändert hat oder nicht.
     * Wenn ja, wird sie im entsprechenden Objekt des Spielers aktualisiert.
     * <p>
     * Gibt die aktuelle Connection zurück
     */
    private WebSocket updateConnectionIfChanged(WebSocket conn, String jwt_heartbeat) {

        // suche nach Spieler zur Verbindung
        Player player = findPlayerOfTokenIngameOrInQueue(jwt_heartbeat);

        // wenn es keinen Spieler in unseren Listen mit diesem Token gibt, ist uns die Connection auch egal
        if (player == null) throw new MemorioRuntimeException("Es existiert kein Spieler mit diesem Token.");

        // wenn kein Spieler gefunden, setze sein Token, damit wir die nächste Message an den Client
        // zwischenspeichern können bis zum nächsten Heartbeat
        lostConnectionJwt.add(jwt_heartbeat);

        // neues Setzen der WebsocketConnection, wenn sie sich unterscheidet
        if (!player.getWebsocketConnection().equals(conn)) {
            logger.warn("Connection has changed. Setting new connection...");
            player.setWebsocketConnection(conn);

            return conn;
        }
        return player.getWebsocketConnection();
    }

    /**
     * Sucht in unseren bestehenden Listen (Warteliste und Match-Liste) nach dem User mit diesem JWT.
     * Wenn es ihn gibt, wird der Spieler zurückgegeben, ansonsten null.
     */
    private Player findPlayerOfTokenIngameOrInQueue(String jwt) {
        // User von Jwt holen
        Optional<User> userForToken = getUserForJwtOrNull(jwt);

        // wenns keinen User mit diesem Jwt Token gibt, Abbruch
        if (userForToken.isEmpty()) throw new MemorioRuntimeException("Für dieses JWT ist kein User registriert");

        // suchen nach Spielern in Warteschlange
        Player playerOfJwt = playerQueue.stream()
                .filter(player -> player.getToken().equals(jwt))
                .findFirst().orElse(null);

        if (playerOfJwt != null) return playerOfJwt;

        // suchen nach Spielern Ingame
        playerOfJwt = matches.stream()
                .flatMap(e -> e.getBothPlayers().stream())
                .filter(playerConn -> playerConn.getToken().equals(jwt))
                .findFirst().orElse(null);

        return playerOfJwt;
    }

    private Optional<User> getUserForJwtOrNull(String jwt) {
        JwtTokenUtil jwtTokenUtil = BeanUtil.getBean(JwtTokenUtil.class);
        UserRepository userRepository = BeanUtil.getBean(UserRepository.class);

        String usernameFromToken = jwtTokenUtil.getUsernameFromToken(jwt);
        return userRepository.findByUsername(usernameFromToken);
    }


    private void sendEndscoreToClientsOfConnection(WebSocket conn) {
        try {
            GameRepository gameRepository = BeanUtil.getBean(GameRepository.class);

            Player player = findPlayerByMatchConnection(conn);

            // erstelle Endscore-Objekt aus Game-Objekt
            Game game = gameHandler.getGame();
            Endscore endscore = new Endscore(game.getUserScores());
            String message = MemorioJsonMapper.getStringFromObject(endscore);

            // sende Endscore an Clients
            player.getSubscriber().getWebsocketConnection().send(message);
            player.getWebsocketConnection().send(message);
            logger.info("Endscore-Objekt erfolgreich an alle Clients gesendet.");

            gameRepository.save(game);
            logger.info("Game-Objekt erfolgreich in der Datenbank gespeichert.");
            logger.info("Game: " + game);

            // ende das Match
            dissolveMatch(player.getMatch());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Versucht, einen Spieler in der Warteschlange zu finden. Falls er nicht existiert, wird null zurückgegeben.
     */
    private Player findPlayerInQueueByConnection(WebSocket connection) {
        return playerQueue.stream()
                .filter(player -> player.getWebsocketConnection() == connection)
                .findFirst().orElse(null);
    }

    /**
     * Sendet Game-Instanz an alle Clients einer Verbindung
     */
    private void sendGameToAllClientsOfConnection(WebSocket conn) {
        try {
            Game game = gameHandler.getGame();
            String message = MemorioJsonMapper.getStringFromObject(game);

            Player player = findPlayerByMatchConnection(conn);
            player.getSubscriber().getWebsocketConnection().send(message);
            player.getWebsocketConnection().send(message);
            logger.info("Game Objekt erfolgreich an alle Clients gesendet.");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * Verifiziert das JWT eines Spielers, setzt ihn in die Warteschlange für ein Spiel und schickt das Game-Objekt
     * zurück, wenn ein zweiter Spieler in der Warteschlange ist.
     */
    private void verifyAndCreateConnection(WebSocket conn, String jwt) {
        Player player = getPlayerForJwt(conn, jwt);
        if (player == null) return;
        logger.info("Spieler verbunden: " + player.getUser().getUsername());

        // Wenn noch kein Spieler mit dem Token vorhanden ist, füge Token hinzu
        boolean playerNotYetInMatch = playerQueue.stream().noneMatch(p -> p.getToken().equals(player.getToken()));
        if (playerNotYetInMatch) playerQueue.add(player);

        try {
            matchPlayer();

            // Game-Objekt erstellen
            Game game = new Game(new Board(), player.getUser(), player.getSubscriber().getUser());
            String message = MemorioJsonMapper.getStringFromObject(game);

            // und an alle Teilnehmer des Matches schicken
            player.getSubscriber().getWebsocketConnection().send(message);
            conn.send(message);
            logger.info("Game Objekt erfolgreich an alle Spieler gesendet.");

            // ganz am Schluss im GameHandlersetzen, wenn alles andere durchgelaufen ist
            gameHandler.setGame(game);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Gibt den passenden Spieler zum Jwt zurück, falls er existiert. Existiert er nicht, wird null zurückgegeben.
     */
    private Player getPlayerForJwt(WebSocket conn, String jwt) {
        Optional<User> userForToken = getUserForJwtOrNull(jwt);
        // todo exception werfen oder so
        if (userForToken.isEmpty()) return null;

        return new Player(conn, userForToken.get(), jwt);
    }

}