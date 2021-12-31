package com.example.javafx.controller;

import com.example.javafx.model.Card;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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

    public void setTurn(String msg){
        turn.setText(msg);
    }

    public void updateScore(int score1, int score2){
        score.setText(score1 + " : " + score2);
    }

}
