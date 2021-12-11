package com.example.javafx.controller;

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
        logo.setFitHeight(35);
        logo.setFitHeight(35);
    }

    private Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/pics");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        return null;
    }

    public void logout(MouseEvent mouseEvent) {
    }
}