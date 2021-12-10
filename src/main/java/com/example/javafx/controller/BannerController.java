package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.util.Timer;

public class BannerController {

    @FXML
    Label text;

    @FXML
    AnchorPane banner;

    @FXML
    StackPane stackP;

    public BannerController(){
    }

    @FXML
    protected void initialize(){
        banner.setVisible(false);
    }

    public void setText(String input, boolean success){
        banner.setVisible(false);
        text.setText(input);
        text.setTextAlignment(TextAlignment.CENTER);
        stackP.setAlignment(text, Pos.CENTER);

        String color = success ? "#6dd06d" : "#d06d6d";
        banner.setBackground(new Background(new BackgroundFill(Paint.valueOf(color),null, null)));

        text.setTextFill(Paint.valueOf("#ffffff"));

        text.setVisible(true);
        banner.setVisible(true);

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
