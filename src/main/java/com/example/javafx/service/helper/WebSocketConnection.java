package com.example.javafx.service.helper;

import java.net.URI;
import java.util.Map;

import com.example.javafx.controller.GameController;
import com.example.javafx.controller.PapaController;
import com.example.javafx.service.GameService;
import com.example.javafx.service.MemorioJsonMapper;
import javafx.fxml.FXMLLoader;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

/**
 * Klasse für Websocket-Kommunikation.
 */
public class WebSocketConnection extends WebSocketClient {

    private SceneManager sceneManager = null;
    private GameService gameService = GameService.getInstance();

    public WebSocketConnection(URI address, SceneManager sceneManager) {
        super(address);
        this.sceneManager = sceneManager;
    }

    /**
     * Wird aufgerufen wenn der Socketserver gestartet wird - sendet das Login SIgnal an den Server
     * @param handshakedata - Vorgegeben, verwendetn wir gerade nicht
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {

        /*
        Ausfuehren des JavaFX UI Render im Mainthread ueber Runlater. vgl Decision Log
        Das muessen wir machen weil der JavaFX Kontext im Mainthread ist. Wenn die Aenderungen in einem
        Childthread machen weis der Mainthread mit dem Kontext nichts darueber. Ueber runLater() fuehren wir die Aufgabe im
        Mainthread aus, sobald er Zeit dafuer hat.
         */
        try {
            javafx.application.Platform.runLater(()->{
                sceneManager.loadGame();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Senden der Loginnachricht im WS Thread
        mSend(MessageKeys.LOGIN, "");
    }

    /**
     * Methode zum senden einer Nachricht
     * @param messageKey Nachrichtenflag - siehe Nachrichtentabelle oder Messagekeyenum
     * @param payload - DIe Payload zur entsprechenden Nachricht
     */
    public void mSend(MessageKeys messageKey, String payload){
        send("{\"" + messageKey + "\":\"" + payload + "\"" +
                ",\"JWT\":" + "\"" + TokenManager.getInstance().getToken() + "\"}");
    }

    /**
     * Senden des beendensignals an den Server
     * @param code Unbenutzt
     * @param reason Unbenutzt
     * @param remote Unbenutzt
     */
    @Override
    public void onClose(int code, String reason, boolean remote){
        //TODO: Checken ob noch was gesendet wird this.mSend(MessageKeys.DISSOLVE, "");
        //System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message){

        try {
            Map<String, String> jsonMap = MemorioJsonMapper.getMapFromString(message);
            //Check
         /*
            if (jsonMap.keySet().size() != 2) {
                throw new RuntimeException("JSON-Keyset muss aus 2 Elementen bestehen..");
            }*/
            handleMessage(jsonMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Testing
    System.out.println("On Message " + message);
    }

    @Override
    public void onError(Exception e){System.out.println(e);}

    //Testmethode zum testen der Verbindung vom Socket zum Gamecontroller
    public void activateGameController(){
        // Herausholen des GameControler aus dem Service
        GameController controller = gameService.getGameController();
        System.out.println("Teste Verschmelzung FUSSSSION");
        controller.newSysMessage("hi from socket");
    }

    /**
     * Nachrichtenhandlen
     * @param payload
     */
    public void handleMessage(Map<String, String> payload){
        System.out.println("Ouput in handle message " + payload);
        if(payload.containsKey("board")){
            System.out.println("Foobar");
            System.out.println(payload.values());

        }
        //Map<String, String> board = payload.get("board")[2];
        System.out.println();
        /* Message handling
        1. Checken ob gameDto oder Enscore dto
            - instanceOf
        2. Wenn game dto
            - checken ob bereits gameDto existiert
            - wenn nicht, lege game an mit infos aus Dto
                - userscore
                - Board
                - wer ist am zug
             - wenn dto bereits vorhanden
                - dann muss das spiel ja bereits laufen
                - durchwandern des bekommen dtos und abgleichen des vorhandenen gamedtos
                - aenderungen umsetzen
         3. WEnn endscore dto,
            - mache endscore dinge
            */

        /*

        MatchDTO kommt rein

        Dto durch iterieren:
            // payload = setBoard
            gameController.setBoard(payload.getBoard());
            gameController.setScore(payload.getScore());
            gameController.setTurn(payload.setTurn());
         */

    }
}