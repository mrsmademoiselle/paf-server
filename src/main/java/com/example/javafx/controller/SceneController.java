package com.example.javafx.controller;

import com.example.javafx.HttpConnector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneController {

    private Stage stage;
    private double APPLICATION_WIDTH;
    private double APPLICATION_HEIGHT;

    // check Auth hier einbauen und auf /register /login prüfen
    private URL loginScene;
    private URL registerScene;
    private URL dashboardScene;
    private URL generalCSS;
    private static final SceneController instance = new SceneController();

    private SceneController() {
    }

    public static SceneController getInstance() {
        return instance;
    }

    public void loadLogin() {
        this.loadScene(this.loginScene);
    }

    public void loadRegistration() {
        this.loadScene(this.registerScene);
    }

    public void loadDashboard() {
        this.loadScene(this.dashboardScene);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage loadStage() {
        return this.stage;
    }

    public void setLoginScene(URL loginScene) {
        this.loginScene = loginScene;
    }

    public void setDashboardScene(URL dashboardScene) {
        this.dashboardScene = dashboardScene;
    }

    public void setRegisterScene(URL registerScene) {
        this.registerScene = registerScene;
    }

    public void setGeneralCSS(URL resource) {
        this.generalCSS = resource;
    }

    public void setScale(double APPLICATION_HEIGHT, double APPLICATION_WIDTH) {
        this.APPLICATION_HEIGHT = APPLICATION_HEIGHT;
        this.APPLICATION_WIDTH = APPLICATION_WIDTH;
    }

    public void loadScene(URL requestedScene) {
        try {

            if (requestedScene.getPath().contains("/login.fxml") || requestedScene.getPath().contains("/register.fxml")) {
                loadView(requestedScene);
            } else {
                // sende authrequest
                // checke request fuer http code
                boolean isAuthenticated = HttpConnector.checkUserAUth();
                URL scene = isAuthenticated ? requestedScene : loginScene;

                loadView(scene);
            }
        } catch (IOException e) {
            System.out.println("Exception!");
            e.printStackTrace();
        }
    }

    private void loadView(URL path) throws IOException {
        Parent view;
        view = FXMLLoader.load(path);

        Scene scene = new Scene(view, this.APPLICATION_WIDTH, this.APPLICATION_HEIGHT);
        scene.getStylesheets().add(generalCSS.toExternalForm());
        stage.setScene(scene);
    }

}