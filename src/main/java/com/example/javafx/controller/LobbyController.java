package com.example.javafx.controller;

import com.example.javafx.service.GameService;
import com.example.javafx.service.helper.FileManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LobbyController extends LayoutController {

    @FXML
    ImageView loading;

    @FXML
    NavbarController navbarController;

    GameService gameService = GameService.getInstance();

    @FXML
    protected void initialize() {
        Image pic = FileManager.getPic("loading.png");
        loading.setImage(pic);
        loading.setFitHeight(50);
        loading.setFitWidth(50);
    }

    public void cancel() {
        gameService.stopLookingForGame();
    }

}