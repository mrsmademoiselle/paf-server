package com.example.javafx.controller;

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

    private URL loginScene;
    private URL registerScene;
    private URL dashboardScene;
    private static final SceneController instance = new SceneController();

    private SceneController() {}

    public static SceneController getInstance(){
        return instance;
    }

    public void loadLogin(){this.loadScene(this.loginScene);}
    public void loadRegistration(){this.loadScene(this.registerScene);}
    public void loadDashboard(){this.loadScene(this.dashboardScene);}
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void setLoginScene(URL loginScene) {this.loginScene = loginScene;}
    public void setDashboardScene(URL dashboardScene) {this.dashboardScene = dashboardScene;}
    public void setRegisterScene(URL registerScene) {
        this.registerScene = registerScene;
    }

    public void setScale(double APPLICATION_HEIGHT, double APPLICATION_WIDTH) {
        this.APPLICATION_HEIGHT = APPLICATION_HEIGHT;
        this.APPLICATION_WIDTH = APPLICATION_WIDTH;
    }

    public void loadScene(URL path){
        Parent view = null;
        try {
            view = FXMLLoader.load(path);
            Scene scene = new Scene(view, this.APPLICATION_WIDTH, this.APPLICATION_HEIGHT);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
