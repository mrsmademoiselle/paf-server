package com.example.javafx.controller;

import com.example.javafx.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends PapaController {

    @FXML
    TextField usernameTextfield;

    @FXML
    PasswordField passwordTextfield;

    @FXML
    BannerController bannerController;


    UserService userService = new UserService();

    @FXML
    protected void initialize() {
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
                usernameTextfield.setStyle("-fx-border-color:#d95252; -fx-border-width: 3;"
                );
                bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            } else {
                usernameTextfield.setStyle("");
            }
        });
    }
}