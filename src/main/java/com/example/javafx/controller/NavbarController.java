package com.example.javafx.controller;

import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.TokenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class NavbarController {

    @FXML
    ImageView logo;

    @FXML
    Button logoutButton;

    @FXML
    protected void initialize() {
        // TODO der kack geht noch nicht
        // ImageView value = new ImageView(getPic("logout.svg"));
        // logoutButton.setGraphic(value);
        setLogoPic();
    }

    private void setLogoPic() {
        logo.setImage(getPic("Brand.png"));
        logo.setFitHeight(30);
        logo.setFitHeight(30);
    }

    private Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/pics");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        return null;
    }

    // TODO evtl verschieben
    public void logout(MouseEvent mouseEvent) {
        TokenManager tokenManager = TokenManager.getInstance();
        SceneManager sceneManager = SceneManager.getInstance();
        tokenManager.clearToken();
        sceneManager.loadLogin();
    }
}