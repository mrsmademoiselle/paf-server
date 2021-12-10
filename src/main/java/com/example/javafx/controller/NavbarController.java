package com.example.javafx.controller;

import com.example.javafx.HttpConnector;
import coresearch.cvurl.io.model.Response;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NavbarController {

    @FXML
    ImageView logo;

    @FXML
    protected void initialize() {
        setLogoPic();
        //checkAuth();
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

}