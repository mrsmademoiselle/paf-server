package com.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.File;

public class AccountDataController {

    @FXML
    Circle profilePic;

    @FXML
    public void initialize() {
        setProfilePic();
    }

    public void setProfilePic() {
        Image pic = getPic("painting2.png");
        if (pic != null) {
            profilePic.setFill(new ImagePattern(pic));
            profilePic.setRadius(100);
        }
    }

    private Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/pics");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        return null;
    }

}