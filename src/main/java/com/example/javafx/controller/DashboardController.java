package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class DashboardController {

    private PreferenceController preferenceController;

    @FXML
    protected void initialize(){
        preferenceController = PreferenceController.getInstance();
        String token = preferenceController.getToken();
        System.out.println(token);
    }
}
