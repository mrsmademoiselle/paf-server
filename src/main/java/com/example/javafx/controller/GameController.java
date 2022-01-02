package com.example.javafx.controller;

import com.example.javafx.model.Card;
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

        final double cardY = (getHeightWithOffset() / CARDS_Y) - WIGGLE;
        final double cardX = cardY;


        gameGrid.setHgap(10);
        gameGrid.setVgap(10);

        logBox.setEditable(false);
        logBox.setBackground(Background.EMPTY);

        for(int x = 0; x < CARDS_X; x++){
            for(int y = 0; y < CARDS_Y; y++) {
                Card card = new Card();
                card.setHeight(cardX);
                card.setWidth(cardY);
                card.setArcHeight(50);
                card.setArcWidth(50);
                card.setFill(Color.GRAY);
                card.setStyle("-fx-cursor: hand");
                card.setOnMouseClicked((new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {onCardFlip(card, event);}
                }));
                Image pic = FileManager.getPic("cardPattern.jpg");
                card.setFill(new ImagePattern(pic));
                gameGrid.add(card, x, y);
            }
        }
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
    public void onCardFlip(Card card, MouseEvent event){
        if(card.getFlipped()){
            card.setFlipped(false);
            renderBackSide(card);
        } else {
            card.setFlipped(true);
            card.setFill(new ImagePattern(new Image(card.getCardSource())));
            newSysMessage(card.getCardSource());
        }
    }

    public void renderBackSide(Card card){
        Image pic = FileManager.getPic("cardPattern.jpg");
        card.setFill(new ImagePattern(pic));
    }
}
