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

    private static double applicationWidth;
    private static double applicationHeight;

    PreferenceController preferenceController =  PreferenceController.getInstance();

    @FXML
    protected void initialize() {
        //this.form.setAlignment(Pos.BASELINE_CENTER);
        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();
        applicationWidth = primaryScreen.getWidth() < 1920 ? primaryScreen.getWidth() : 1920;
        applicationHeight = primaryScreen.getHeight() < 1080 ? primaryScreen.getHeight() : 1080;

        form.setLayoutX(applicationWidth / 3);
        form.setLayoutY(applicationHeight / 9);

        title.setLayoutX(form.getLayoutX() + 110);
        title.setTranslateY(form.getLayoutY() / 4);
        title.toFront();
    }

    public void sendToRegistration() {
        SceneController sceneController = SceneController.getInstance();
        sceneController.loadRegistration();
    }

    public void login() {
        Map<String, String> response = new HashMap<String, String>();
        String jwt = "";
        SceneController sceneController = SceneController.getInstance();
        // return if input fields empty
        if (this.username.getText().isBlank() || this.password.getText().isBlank()) {
            return;
        }
        UserAuthDto userAuthDto = new UserAuthDto(this.username.getText(), this.password.getText());
        // post login request
        response = HttpConnector.post("user/login", userAuthDto);
        int responseCode = Integer.parseInt(response.get("code"));
        // return if login failed
        // send to dashboard on success
        if (responseCode != 200) {
            System.out.println("LoginController: Response Code 200");

            sceneController.loadLogin();
            return;
        } else {
            sceneController.loadLogin();
        }

    }
}