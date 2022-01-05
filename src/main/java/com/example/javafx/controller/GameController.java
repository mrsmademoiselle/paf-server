package com.example.javafx.controller;
import com.example.javafx.model.*;
import com.example.javafx.service.GameService;
import com.example.javafx.service.helper.FileManager;
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

import java.util.Map;


public class GameController extends PapaController {

    @FXML
    Text score;

    @FXML
    Text turn;

    @FXML
    ListView logBox;

    @FXML
    NavbarController navbarController;

    @FXML
    GridPane gameGrid;

    private int CARDS_X = 4;
    private int CARDS_Y = 4;
    private int WIGGLE = 30;


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

        //setBoard();

        logBox.setEditable(false);
        logBox.setBackground(Background.EMPTY);
        updateScore(0, 0);

        setTurn("Niemand ist dran!");
        newSysMessage("Field initialized hahaha");
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
        if(card.getFlipped()){
            card.setFlipped(false);
            // Rueckseite anzeigen
            renderBackSide(card);
        } else {
            // Bild vom Server anzeigen
            card.setFlipped(true);
            card.setFill(new ImagePattern(new Image(card.getCardSource())));
            newSysMessage(card.getCardSource());
        }
    }

    /**
     * Rendert die leere Rueckseite einer Karte
     * @param card Karte deren Rueckseite angezeigt werden soll
     */
    public void renderBackSide(Card card){
        Image pic = FileManager.getPic("cardPattern.jpg");
        card.setFill(new ImagePattern(pic));
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
                card.setFill(Color.GRAY);
                card.setStyle("-fx-cursor: hand");
                // styling end
                card.setOnMouseClicked((new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {onCardFlip(card, event);}
                }));
                renderBackSide(card);
                // Setzen der Farben - wir ziehen uns die Pair ID und verarbeiten sie in die CardSource
                JSONObject jCard = (JSONObject) cardSet.get(counter);
                // Setzend er Cardsource, muss hier passieren
                card.setCardSource("http://localhost:9090/public/"+ jCard.get("pairId") +".jpg");
                gameGrid.add(card, x, y);
                card.setCardId("" + jCard.get("pairId"));
                counter++;
            }
        }
    }

    //TODO: remove methods or do stuff
    public void setTurn(PlayerDto playerDto){}
    public void setScore(ScoreDto scoreDto){}

    //Testing 22:30
    public void digestGame(JSONObject message){
        //ggf umbenen von digestGame zu digestGameMessage
        // ggf. umbauen das GameDTO empfangen wird
        // 1. Wie kommen wir an die Informationen ran
        // 2. wie bringen wir die informationen in das ui

        /*
        System.out.println(payload.getClass());
        System.out.println(payload);
        String _key = "board";
        System.out.println("board is drin: " + payload.containsKey(_key));
        System.out.println(payload.get(_key));
        System.out.println("Printe Keys:" + payload.keySet());
        for(Object key : payload.keySet()){
            System.out.println(key.getClass());
            }
         */

        if(message.has("board")){
            //do the board stuff
            JSONObject board = (JSONObject)message.get("board");
            JSONArray cardset = (JSONArray)board.get("cardSet");
            setBoard(cardset);

        }else if (message.has("endscore")) {
            //do the endscorestuff
            System.out.println("Ich bekam ein Endscore");
        }

    }
}
