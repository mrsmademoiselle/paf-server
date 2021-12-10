package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class BannerController {

    @FXML
    Text text;

    public BannerController(String text){
        this.text.setText(text);
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
