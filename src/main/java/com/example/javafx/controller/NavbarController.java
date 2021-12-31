package com.example.javafx.controller;

import com.example.javafx.service.GameService;
import com.example.javafx.service.helper.FileManager;
import com.example.javafx.service.helper.SceneManager;
import com.example.javafx.service.helper.TokenManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URISyntaxException;

public class NavbarController extends PapaController {

    @FXML
    ImageView logo;

    @FXML
    Button logoutButton;

    SceneManager sceneManager = SceneManager.getInstance();
    GameService gameService = GameService.getInstance();

    @FXML
    protected void initialize() {
        setLogoPic();
        setLogoutButtonPic();
    }

    private void setLogoutButtonPic() {
        // aus irgendeinem javafx-Grund funktioniert das Setzen eines Bilds in einem Button nur mit pngs, nicht mit svgs
        Image pic = FileManager.getPic("button_logout.png");
        ImageView img = new ImageView(pic);
        img.setFitHeight(20);
        img.setFitWidth(20);
        logoutButton.setGraphic(img);
    }

    private void setLogoPic() {
        Image pic = FileManager.getPic("logo.png");
        logo.setImage(pic);
        logo.setFitHeight(30);
        logo.setFitHeight(30);
    }

    // TODO evtl verschieben
    public void logout() {
        TokenManager tokenManager = TokenManager.getInstance();
        SceneManager sceneManager = SceneManager.getInstance();
        tokenManager.clearToken();
        sceneManager.loadLogin();
    }

    public void redirectToProfile() {
        sceneManager.loadProfile();
    }

    public void redirectToHistory() {
        sceneManager.loadHistory();
    }

    public void startGame() {
        try {
            gameService.lookForGame();
        } catch (URISyntaxException e){
            System.out.println(e);
        }
    }
}