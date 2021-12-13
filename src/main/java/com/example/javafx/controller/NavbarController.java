package com.example.javafx.controller;

import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.TokenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;

public class NavbarController extends PapaController {

    @FXML
    ImageView logo;

    @FXML
    Button logoutButton;

    @FXML
    protected void initialize() {
        setLogoPic();
        setLogoutButtonPic();

    }

    private void setLogoutButtonPic() {
        // aus irgendeinem javafx-Grund funktioniert das Setzen eines Bilds in einem Button nur mit pngs, nicht mit svgs
        ImageView img = new ImageView(getPic("logout_png.png"));
        img.setFitHeight(20);
        img.setFitWidth(20);
        logoutButton.setGraphic(img);
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