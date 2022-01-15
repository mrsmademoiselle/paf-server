package com.example.javafx.service.helper;

import com.example.javafx.controller.GameController;
import com.example.javafx.service.GameService;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Klasse für Websocket-Kommunikation.
 */
public class WebSocketConnection extends WebSocketClient {

    private SceneManager sceneManager = null;
    private GameService gameService = GameService.getInstance();
    // Checken ob das Spiel schon am laufen ist
    private boolean firstGame = false;
    private boolean continueHeartbeat = true;

    public WebSocketConnection(URI address, SceneManager sceneManager) {
        super(address);
        this.sceneManager = sceneManager;
    }

    /**
     * Wird aufgerufen wenn der Socketserver gestartet wird - sendet das Login SIgnal an den Server
     *
     * @param handshakedata - Vorgegeben, verwendetn wir gerade nicht
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        mSend(MessageKeys.LOGIN, "");
        this.continueHeartbeat = true;
        heartbeat();
    }

    /**
     * Methode zum senden einer Nachricht
     *
     * @param messageKey Nachrichtenflag - siehe Nachrichtentabelle oder Messagekeyenum
     * @param payload    - DIe Payload zur entsprechenden Nachricht
     */
    public void mSend(MessageKeys messageKey, String payload) {
        send("{\"" + messageKey + "\":\"" + payload + "\"" +
                ",\"JWT\":" + "\"" + TokenManager.getInstance().getToken() + "\"}");
    }

    public void heartbeat() {
        if (continueHeartbeat) {
            mSend(MessageKeys.HEARTBEAT, null);
            // jede Sekunde wird ein Heartbeat gesendet
            TimerTask task = new TimerTask() {
                public void run() {
                    heartbeat();
                }
            };
            Timer timer = new Timer("Heartbeat Timer");
            timer.schedule(task, 1000L);
        }
    }

    /**
     * Senden des beendensignals an den Server
     *
     * @param code   Unbenutzt
     * @param reason Unbenutzt
     * @param remote Unbenutzt
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        //TODO: Checken ob noch was gesendet wird this.mSend(MessageKeys.DISSOLVE, "");
        //System.out.println("closed with exit code " + code + " additional info: " + reason);
        this.continueHeartbeat = false;
    }

    @Override
    public void onMessage(String message) {

        if (!this.firstGame) {
            this.firstGame = true;
        /*
        Ausfuehren des JavaFX UI Render im Mainthread ueber Runlater. vgl Decision Log
        Das muessen wir machen weil der JavaFX Kontext im Mainthread ist. Wenn die Aenderungen in einem
        Childthread machen weis der Mainthread mit dem Kontext nichts darueber. Ueber runLater() fuehren wir die Aufgabe im
        Mainthread aus, sobald er Zeit dafuer hat.
         */
            try {
                javafx.application.Platform.runLater(() -> {
                    sceneManager.loadGame();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Senden der Loginnachricht im WS Thread
        }

        // Nachrichtenhandling - Die Nachricht wird zum JSON und weiter an handlemessag uebergeben
        JSONObject jo = new JSONObject(message);
        handleMessage(jo);
        //System.out.println("Message wurde verarbeitet " + message);
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e);
    }

    //Testmethode zum testen der Verbindung vom Socket zum Gamecontroller
    public void activateGameController() {
        // Herausholen des GameControler aus dem Service
        GameController controller = gameService.getGameController();
        System.out.println("Teste Verschmelzung FUSSSSION");
        controller.newSysMessage("hi from socket");
    }

    /**
     * Nachrichtenhandlen Zwischenstation im Websocket fuer das behandeln der Nachrichten
     *
     * @param message Eingehende Nachricht aus WS
     */
    public void handleMessage(JSONObject message) {
        //Dafuer sorgen das die Nachricht nicht abgehandelt wird BEVOR der Gamecontroller durch den
        //Mainthread gerendert wird, sonst kommt es zu einer Exception
        while (gameService.getGameController() == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Reinziehen des Gamecontrollers
        GameController controller = gameService.getGameController();

        // Update des Ui wieder in den Mainthread lagern
        try {
            javafx.application.Platform.runLater(() -> {
                controller.digestGame(message);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}