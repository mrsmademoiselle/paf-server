package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.Window;

public class LayoutController {

    @FXML
    public HBox mainContent;

    @FXML
    BannerController bannerController;

    @FXML
    NavbarController navbarController;

    private static LayoutController instance;

    @FXML
    protected void initialize(){
    }

    private LayoutController(){

    }

     public static LayoutController getInstance(){
        if (instance == null){
            instance = new LayoutController();
        }
        return instance;
    }

    public void setView(Node view){
        // Nehme eine View entgegen und packe sie in die eigene Box und zeige sie an
        mainContent.getChildren().add(view);

    }
}
