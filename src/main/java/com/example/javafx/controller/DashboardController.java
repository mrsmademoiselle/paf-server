package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class DashboardController {

    private PreferenceController preferenceController = new PreferenceController();

    @FXML
    protected void initialize(){
        String token = preferenceController.getToken();
        System.out.println(token);
    }
}
