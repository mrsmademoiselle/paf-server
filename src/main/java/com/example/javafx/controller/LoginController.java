package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    Label title;

    @FXML
    VBox form;

    private static double applicationWidth;
    private static double applicationHeight;

    public void login(){

    }

    public void sendToRegistration(){
        SceneController sceneController = SceneController.getInstance();
        sceneController.loadRegistration();
    }

    private void setFormLayout() {
        // erstmal workaround
        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();
        applicationWidth = primaryScreen.getWidth() < 1920 ? primaryScreen.getWidth() : 1920;
        applicationHeight = primaryScreen.getHeight() < 1080 ? primaryScreen.getHeight() : 1080;

        form.setLayoutX(applicationWidth / 3);
        form.setLayoutY(applicationHeight / 9);

        title.setLayoutX(form.getLayoutX() + 110);
        title.setTranslateY(form.getLayoutY() / 4);
        title.toFront();
    }
}
