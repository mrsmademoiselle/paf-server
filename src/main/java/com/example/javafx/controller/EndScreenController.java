package com.example.javafx.controller;

import com.example.javafx.service.GameService;
import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class EndScreenController extends LayoutController {
    GameService gameService = GameService.getInstance();

    @FXML
    Text finalText;

    @FXML
    protected void initialize() {
        if (gameService.getWinner() == null) {
            finalText.setText("Es ist unentschieden!");
        } else {
            finalText.setText(gameService.getWinner() + " hat gewonnen!");
        }
    }
}
