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
    }
}
