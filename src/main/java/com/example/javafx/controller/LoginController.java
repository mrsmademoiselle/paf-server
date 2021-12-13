package com.example.javafx.controller;

import com.example.javafx.service.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class LoginController extends PapaController {

    @FXML
    Label title;

    @FXML
    VBox form;

    @FXML
    TextField usernameTextfield;

    @FXML
    PasswordField passwordTextfield;

    @FXML
    BannerController bannerController;

    private static double applicationWidth;
    private static double applicationHeight;

    UserService userService = new UserService();

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

        activateInputListener();
    }

    public void sendToRegistration() {
        userService.redirectToRegister();
    }

    public void login() {
        // return if input fields empty
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        if (username.isBlank() || password.isBlank()) {
            bannerController.setText("Username und Passwort dürfen nicht leer sein.", false);
        }
        userService.loginUser(username, password);

        // diesen Text erreichen wir nur, wenn Login nicht erfolgreich war
        bannerController.setText("User kann nicht eingeloggt werden", false);

    }

    private void activateInputListener() {
        // Livevalidierung
        usernameTextfield.textProperty().addListener((obs, oldInput, newInput) -> {
            if (!usernameTextfield.getText().matches("[\\w|\\d]*")) {
                usernameTextfield.setStyle("-fx-border-color:#d95252;" +
                        "-fx-border-width: 3;"
                );
                bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            } else {
                usernameTextfield.setStyle("");
            }
        });
    }
}