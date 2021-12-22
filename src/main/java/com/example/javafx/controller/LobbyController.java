package com.example.javafx.controller;

import com.example.javafx.service.helper.FileManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LobbyController extends PapaController {

    @FXML
    ImageView loading;

    @FXML
    NavbarController navbarController;

    @FXML
    protected void initialize() {
        Image pic = FileManager.getPic("loading.png");
        loading.setImage(pic);
        loading.setFitHeight(50);
        loading.setFitWidth(50);
    }
}