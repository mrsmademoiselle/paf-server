package com.example.javafx.service.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class SceneManager {

    private Stage stage;
    private double APPLICATION_WIDTH;
    private double APPLICATION_HEIGHT;

    // check Auth hier einbauen und auf /register /login prüfen
    private URL loginScene;
    private URL registerScene;
    private URL lobbyScene;
    private URL profile;
    private URL generalCSS;
    private URL gameScene;
    private URL endScreen;
    private static final SceneManager instance = new SceneManager();

    // Aktuelle Scene
    private String currentScene;

    private SceneManager() {
    }

    public static SceneManager getInstance() {
        return instance;
    }

    public String getCurrentScene(){return this.currentScene;}

    // Laden von Scenen/Views
    public void loadLogin() {
        this.currentScene = "login";
        this.loadScene(this.loginScene);
    }

    public void loadRegistration() {
        this.currentScene = "registration";
        this.loadScene(this.registerScene);
    }

    public void loadLobby() {
        this.currentScene = "lobby";
        this.loadScene(this.lobbyScene);
    }

    public void loadProfile() {
        this.currentScene = "profile";
        this.loadScene(this.profile);
    }

    public void loadEdscreen(){
        this.currentScene = "edscreen";
        this.loadScene(this.endScreen);
    }

    public void loadHistory() {
        this.currentScene = "history";
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Setzen der Scenen/Views
    public void setProfileScene(URL stage) {
        this.profile = stage;
    }

    public void setEndScreen(URL stage) {this.endScreen = stage;}

    public Stage loadStage() {
        return this.stage;
    }

    public void setLoginScene(URL loginScene) {
        this.loginScene = loginScene;
    }

    public void setLobbyScene(URL lobbyScene) {
        this.lobbyScene = lobbyScene;
    }

    public void setRegisterScene(URL registerScene) {
        this.registerScene = registerScene;
    }

    public void loadGame() {
        this.currentScene ="game";
        this.loadScene(this.gameScene);
    }

    public void setGameScene(URL gameScene) {
        this.gameScene = gameScene;
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