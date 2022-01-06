package com.example.javafx.controller;

import com.example.javafx.service.GameService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class EndScreenController extends PapaController {
    GameService gameService = GameService.getInstance();

    @FXML
    Text finalText;

    @FXML
    protected void initialize(){
        finalText.setText(gameService.getWinner());
    }
}
