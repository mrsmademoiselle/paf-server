package com.example.javafx.controller;
import com.example.javafx.model.*;
import com.example.javafx.service.GameService;
import com.example.javafx.service.helper.FileManager;
import com.example.javafx.service.helper.MessageKeys;
import com.example.javafx.service.helper.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.ByteArrayInputStream;


public class GameController extends PapaController {

    @FXML
    Text score;

    @FXML
    Text turn;

    @FXML
    ListView logBox;

    @FXML
    Rectangle pImg1;

    @FXML
    Rectangle pImg2;

    @FXML
    NavbarController navbarController;

    @FXML
    GridPane gameGrid;

    private int CARDS_X = 4;
    private int CARDS_Y = 4;
    private int WIGGLE = 30;
    private String username;
    private boolean isThisUserTurn = false;


    @FXML
    public void initialize() {
        // Verschmelzen von Gamecontroller instanz mit GameService singleton
        GameService gameService = GameService.getInstance();
        // Uebergeben der eigenen GameController instanz
        gameService.setGameController(this);
        /* Aufrufen einer Testmethode im Gameservice
        In der Testmethode wird der Websocket aufgerufen
         */
        gameService.testActivatedGameController();
        // Herauslesen des Usernamen um zu pruefen ob der User zuerst dran ist
        username = gameService.getUsernamebyToken();

        logBox.setEditable(false);
        logBox.setBackground(Background.EMPTY);
        updateScore(0, 0);

        setTurn("Suche Spiele...");
        newSysMessage("Sucht nach Spielen...");
    }

    public void newSysMessage(String msg){
        Text text = new Text();
        text.setText(msg);
        logBox.getItems().add(text);
    }

    public void setTurn(String msg){turn.setText(msg);}
    public void updateScore(int score1, int score2){score.setText(score1 + " : " + score2);}

    /**
     * Flippen der karte
     * @param card Karte die gedreht werden soll
     * @param event Event
     */
    public void onCardFlip(Card card, MouseEvent event){

        if(!card.getFlipped()){
            // Bild vom Server anzeigen
            renderFront(card);
            // Senden der geflippten Card auf dem Server
            GameService gameService = GameService.getInstance();
            gameService.getWebSocketConnection().mSend(
                    MessageKeys.FLIPPED, card.getCardId()
            );
        }
    }

    /**
     * Rendert die leere Rueckseite einer Karte
     * @param card Karte deren Rueckseite angezeigt werden soll
     */
    public void renderBackSide(Card card){
        card.setFlipped(false);
        Image pic = FileManager.getPic("cardPattern.jpg");
        card.setFill(new ImagePattern(pic));
    }
    public void renderFront(Card card){
        card.setFlipped(true);
        card.setFill(new ImagePattern(new Image(card.getCardSource())));
        newSysMessage(card.getCardSource());
    }

    /**
     * Setzen und updaten des Boards
     * @param
     */
    public void setBoard(JSONArray cardSet){

        gameGrid.setHgap(10);
        gameGrid.setVgap(10);

        final double cardY = (getHeightWithOffset() / CARDS_Y) - WIGGLE;
        final double cardX = cardY;

        int counter = 0;
        for(int y = 0; y < CARDS_Y; y++){
            for(int x = 0; x < CARDS_X; x++) {
                Card card = new Card();
                // styling
                card.setHeight(cardX);
                card.setWidth(cardY);
                card.setArcHeight(50);
                card.setArcWidth(50);
                card.setStyle("-fx-cursor: hand");
                // styling end

                //Handeln der Kartenturns - wenn user nicht dran passiert bei Klick auf Karten nichts
                card.setOnMouseClicked((new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(isThisUserTurn){
                            onCardFlip(card, event);
                        }
                    }
                }));

                // Setzen der Farben - wir ziehen uns die Pair ID und verarbeiten sie in die CardSource
                JSONObject jCard = (JSONObject) cardSet.get(counter);
                // Setzend er Cardsource, muss hier passieren da wir jedes mal das Board rendern sonst ist das Feld leer
                card.setCardSource("http://localhost:9090/public/"+ jCard.get("pairId") +".jpg");
                // Setzen des Flipped Status
                String flipped = jCard.get("flipStatus").toString();

                switch(flipped) {
                    case "NOT_FLIPPED":
                        renderBackSide(card);
                        break;
                    case "FLIPPED":
                        renderFront(card);
                        break;
                    case "WAITING_TO_FLIP":
                        renderFront(card);
                        break;
                    default:
                        card.setFill(Color.GRAY);
                        break;
                }

                // Hinzufuegen der Karte auf dem Grid
                gameGrid.add(card, x, y);
                card.setPairId("" + jCard.get("pairId"));
                // setzen der CardID fuer den Server
                card.setCardId("" + jCard.get("id"));
                counter++;
            }
        }
    }

    /**
     * Verarbeiten der Gamenachricht und vorbereiten fuer weiteres Handling
     * @param message Nachricht aus WS
     */
    public void digestGame(JSONObject message){
        // Unterscheidung Spiel und Endscore

        // Wenn die Nachricht ein Board enthaelt ist es entweder die aller erste Nachricht oder eine Gamenachricht
        // Es muss also der User bestimmt werden der dran ist und das Board aktualisiert werden
        if(message.has("board")) {

            // Herauslesen des Scores und des aktuellen Zuges
            JSONObject turn = (JSONObject)message.get("currentTurn");
            JSONArray scores = (JSONArray)message.get("userScores");

            //Handeln des ersten zuges und blockieren der Karten
            if(turn.get("username").toString().equals(this.username)){
                this.isThisUserTurn = true;
            } else {
                System.out.println("User ist nicht dran: " + turn.get("username"));
                this.isThisUserTurn = false;
            }

            //do the board stuff
            JSONObject board = (JSONObject)message.get("board");
            JSONArray cardset = (JSONArray)board.get("cardSet");

            // uebergen der Karten auf das Board
            setBoard(cardset);
            setTurn("Spieler: " + turn.get("username") + " ist dran!");

            // Setzen der Scores
            JSONObject s1 = (JSONObject)scores.get(0);
            JSONObject s2 = (JSONObject)scores.get(1);
            updateScore(((Integer) s1.get("moves")), (Integer) s2.get("moves"));

            // Setzen der Bilder
            setUserMatchImages(s1,1);
            setUserMatchImages(s2,2);

            // handling vom Endscore objekt
        } else if (message.has("winner")) {
            //do the endscorestuff
            JSONObject winner = (JSONObject)message.get("winner");
            // Setzen des Siegers
            setTurn("Sieger ist: " + (String) winner.get("username"));
            // Beenden der WS-Verbindung und Thread beenden
            GameService gameService = GameService.getInstance();
            gameService.stop();
            // Winner im Gameservice setzen
            gameService.setWinner((String) winner.get("username"));
            // Weiterleiten auf Endgamescreen
            SceneManager.getInstance().loadEdscreen();
        }
    }

    /**
     * Setzen der Spielerbilder
     * @param jo JSON von dem User
     * @param counter Feld wo es gesetzt werden soll
     */
    private void setUserMatchImages(JSONObject jo, int counter){
        // Herausholen des user-teils aus dem JO
        JSONObject userJO = (JSONObject)jo.get("user");

        // Laden des Bildes, bzw nicht laden des Bildes
        byte[] image = {1};
        if (userJO.get("image") != null){
            image = java.util.Base64.getDecoder().decode(userJO.getString("image"));
        }
        // Wenn es kein Bild im JSON gab, ist image durch die vorherige Pruefung leer, daher laden des Defaultbildes
        if (image.length < 100){
            Image pic = FileManager.getPic("default.jpg");
            // Bilder Setzen, abhaengig wo wir es setzen wollen
            if (counter == 1){
                pImg1.setFill(new ImagePattern(pic));
            }
            if (counter == 2) {
                pImg2.setFill(new ImagePattern(pic));
            }
        }else{
            // Bilder Setzen, abhaengig wo wir es setzen wollen, wenn Bild da ist
            if (counter == 1){
            pImg1.setFill(new ImagePattern(new Image (new ByteArrayInputStream(image))));
        }
        if (counter == 2) {
            pImg2.setFill(new ImagePattern(new Image(new ByteArrayInputStream(image))));
        }
        }
    }
}
