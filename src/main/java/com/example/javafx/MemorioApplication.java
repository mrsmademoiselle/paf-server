package com.example.javafx;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MemorioApplication extends javafx.application.Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("view/register.fxml"));

        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();

        // bei kleineren Bildschirmen kleineres Fenster, aber max. 1920x1080
        double applicationWidth = primaryScreen.getWidth() < 1920 ? primaryScreen.getWidth() : 1920;
        double applicationHeight = primaryScreen.getHeight() < 1080 ? primaryScreen.getHeight() : 1080;

        // Fenster zentrieren
        stage.setX((primaryScreen.getWidth() - applicationWidth) / 2);
        stage.setY((primaryScreen.getHeight() - applicationHeight) / 2);

        Scene scene = new Scene(view, applicationWidth, applicationHeight);
        stage.setScene(scene);
        stage.show();
    }
}