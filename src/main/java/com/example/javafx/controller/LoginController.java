package com.example.javafx.controller;

import com.example.javafx.HttpConnector;
import com.example.javafx.model.UserAuthDto;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class LoginController {

    @FXML
    Label title;

    @FXML
    VBox form;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    BannerController bannerController;

    private static double applicationWidth;
    private static double applicationHeight;

    @FXML
    protected void initialize() {
        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();
        applicationWidth = primaryScreen.getWidth() < 1920 ? primaryScreen.getWidth() : 1920;
        applicationHeight = primaryScreen.getHeight() < 1080 ? primaryScreen.getHeight() : 1080;

        form.setLayoutX(applicationWidth / 3);
        form.setLayoutY(applicationHeight / 9);

        title.setLayoutX(form.getLayoutX() + 110);
        title.setTranslateY(form.getLayoutY() / 4);
        title.toFront();

        // Livevalidierung
        username.textProperty().addListener((obs, oldInput, newInput) -> {
            if (!username.getText().matches("[\\w|\\d]*")){
                username.setStyle("-fx-border-color:#d95252;"+
                        "-fx-border-width: 10;"
                );
                bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            }else{
                username.setStyle("");
            }
        });
    }

    public void sendToRegistration() {
        SceneController sceneController = SceneController.getInstance();
        sceneController.loadRegistration();
    }

    public void login() {
        SceneController sceneController = SceneController.getInstance();
        // return if input fields empty
        if (this.username.getText().isBlank() || this.password.getText().isBlank()) {
            bannerController.setText("Username und Passwort dürfen nicht leer sein.", false);
        }

        UserAuthDto userAuthDto = new UserAuthDto(this.username.getText(), this.password.getText());
        // post login request

        boolean isOk = HttpConnector.post("user/login", userAuthDto);
        if (isOk){
            sceneController.loadDashboard();
        }else {
            bannerController.setText("User kann nicht eingeloggt werden", false);
        }

    }
}