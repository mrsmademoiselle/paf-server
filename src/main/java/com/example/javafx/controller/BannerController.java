package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public class BannerController extends LayoutController {

    @FXML
    Label text;

    @FXML
    Pane banner;
 
    @FXML
    StackPane stackP;

    public BannerController() {
    }

    @FXML
    protected void initialize() {
        banner.setVisible(false);
    }

    public void setText(String input, boolean success) {
        banner.setVisible(false);
        text.setText(input);

        String color = success ? "#6dd06d" : "#d06d6d";
        banner.setBackground(new Background(new BackgroundFill(Paint.valueOf(color), null, null)));

        text.setTextFill(Paint.valueOf("#ffffff"));

        banner.setVisible(true);
        text.setVisible(true);

        // nach 5 Sekunden deaktivieren des Banners - gekapselt in eigenem Thread
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        banner.setVisible(false);
                    }
                },
                5000
        );


    }

}