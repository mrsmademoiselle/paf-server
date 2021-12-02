package com.example.javafx.controller;

import com.example.javafx.HttpConnector;
import com.example.javafx.model.UserAuthDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    @FXML
    Label title;

    @FXML
    VBox form;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    private static double applicationWidth;
    private static double applicationHeight;


    public void sendToRegistration(){
        SceneController sceneController = SceneController.getInstance();
        sceneController.loadRegistration();
    }

    public void login(){
        boolean successfullLogin = false;
        Map<String, String> response = new HashMap<String, String>();
        String jwt = "";
        if(this.username.getText().isBlank() || this.password.getText().isBlank()){return;}
        UserAuthDto userAuthDto = new UserAuthDto(this.username.getText(), this.password.getText()) ;
        response = HttpConnector.post("user/login", userAuthDto);
    }
}
