package com.example.javafx;

import com.example.javafx.service.helper.SceneManager;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MemorioApplication extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        //Parent view = FXMLLoader.load(getClass().getResource("view/register.fxml"));
        //Parent view = FXMLLoader.load(getClass().getResource("view/login.fxml"));

        SceneManager sceneManager = SceneManager.getInstance();

        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();

        // bei kleineren Bildschirmen kleineres Fenster, aber max. 1920x1080
        double applicationWidth = primaryScreen.getWidth() < 1920 ? primaryScreen.getWidth() : 1920;
        double applicationHeight = primaryScreen.getHeight() < 1080 ? primaryScreen.getHeight() : 1080;

        sceneManager.setScale(applicationWidth, applicationHeight);
        sceneManager.setStage(stage);

        //TODO: Move into scene conntroller
        sceneManager.setLoginScene(getClass().getResource("view/login.fxml"));
        sceneManager.setRegisterScene(getClass().getResource("view/register.fxml"));
        sceneManager.setLobbyScene(getClass().getResource("view/lobby.fxml"));
        sceneManager.setAccountDataScene(getClass().getResource("view/account_data.fxml"));
        sceneManager.setGeneralCSS(getClass().getResource("css/general.css"));


        // Fenster zentrieren
        stage.setX((primaryScreen.getWidth() - applicationWidth) / 2);
        stage.setY((primaryScreen.getHeight() - applicationHeight) / 2);
        stage.setWidth(applicationWidth);
        stage.setHeight(applicationHeight);

        System.out.println("MemorioApplication: loadLogin");
        // to be changed to login
        sceneManager.loadRegistration();
        stage.show();
    }
}