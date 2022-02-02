package com.example.javafx;

import com.example.javafx.service.GameService;
import com.example.javafx.service.helper.SceneManager;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MemorioApplication extends javafx.application.Application {

    GameService gameService = GameService.getInstance();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        SceneManager sceneManager = SceneManager.getInstance();

        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();

        // bei kleineren Bildschirmen kleineres Fenster, aber max. 1920x1080
        double applicationWidth = primaryScreen.getWidth() < 1920 ? primaryScreen.getWidth() : 1920;
        double applicationHeight = primaryScreen.getHeight() < 1080 ? primaryScreen.getHeight() : 1080;

        sceneManager.setScale(applicationHeight, applicationWidth);
        sceneManager.setStage(stage);

        sceneManager.setLoginScene(getClass().getResource("view/login.fxml"));
        sceneManager.setRegisterScene(getClass().getResource("view/register.fxml"));
        sceneManager.setLobbyScene(getClass().getResource("view/lobby.fxml"));
        sceneManager.setGameScene(getClass().getResource("view/game.fxml"));
        sceneManager.setProfileScene(getClass().getResource("view/profile.fxml"));
        sceneManager.setGeneralCSS(getClass().getResource("css/general.css"));
        sceneManager.setEndScreen(getClass().getResource("view/endscreen.fxml"));
        sceneManager.setHistoryScene(getClass().getResource("view/history.fxml"));

        // Fenster zentrieren
        stage.setX((primaryScreen.getWidth() - applicationWidth) / 2);
        stage.setY((primaryScreen.getHeight() - applicationHeight) / 2);
        stage.setWidth(applicationWidth);
        stage.setHeight(applicationHeight);

        System.out.println("MemorioApplication: loadLogin");
        sceneManager.loadLogin();
        stage.show();
    }

    @Override
    public void stop() {
        gameService.stop();
    }

}