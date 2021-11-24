package com.example.javafx.controller;

import com.example.javafx.model.UserDto;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RegistrationController {

    // Bedeutet, dass dieses Feld in der fxml-Datei referenziert wird
    @FXML
    Button submit;

    @FXML
    Button editProfilePic;

    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    Label loginLink;

    @FXML
    Circle profilePic;

    /* Einbindung der Navbar im FXML -> nicht löschen */
    @FXML
    NavbarController navbarController;

    @FXML
    Label title;

    @FXML
    AnchorPane page;

    @FXML
    VBox form;

    private static double applicationWidth;
    private static double applicationHeight;


    @FXML
    protected void initialize() {
        setDefaultProfilePic();
        setButtonPic();

        // TODO: Werte müssen responsive gemacht werden (?)
        setFormLayout();
        // Werte haben keine Bedeutung, hat nur bei mir gepasst
        editProfilePic.setTranslateY(editProfilePic.getLayoutY() - 25);
        editProfilePic.setTranslateX(editProfilePic.getLayoutX() + 85);
    }

    private void setFormLayout() {
        // erstmal workaround
        Rectangle2D primaryScreen = Screen.getPrimary().getVisualBounds();
        applicationWidth = primaryScreen.getWidth() < 1920 ? primaryScreen.getWidth() : 1920;
        applicationHeight = primaryScreen.getHeight() < 1080 ? primaryScreen.getHeight() : 1080;

        form.setLayoutX(applicationWidth / 3);
        form.setLayoutY(applicationHeight / 9);

        title.setLayoutX(form.getLayoutX() + 110);
        title.setTranslateY(form.getLayoutY() / 4);
        title.toFront();

    }

    public void editProfilePic(ActionEvent event) {
        notYetImplemented();
    }

    public void register(ActionEvent event) {
        // TODO pw hashing
        UserDto userDto = new UserDto(0L, username.getText(), password.getText());
        sendRequest(userDto);

        // Todo Einloggen als der Benutzer und auf Startseite weiterleiten

    }

    public void sendToLogin() {
        // To be done, wenn Login implementiert wird
        notYetImplemented();
    }

    private void notYetImplemented() {
        Popup popup = new Popup();
        Label e1 = new Label("Das ist noch nicht implementiert.");
        e1.setTextFill(Color.WHITE);
        e1.setBackground(new Background(new BackgroundFill(Paint.valueOf("#FF0000"), null, null)));
        popup.getContent().add(e1);

        // nach 3 Sekunden ausblenden
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(e -> popup.hide());

        popup.centerOnScreen();
        popup.show(page.getScene().getWindow());
        delay.play();
    }

    private void sendRequest(UserDto userDto) {
        notYetImplemented();
    }

    private void setButtonPic() {
        editProfilePic.setGraphic(new ImageView(getPic("edit.png")));
    }

    public void hoverOverProfilePic(){
        Image pic = getPic("upload.png");
        profilePic.setFill(new ImagePattern(pic));
        profilePic.setRadius(100);
        profilePic.setCursor(Cursor.HAND);
    }
    public void setDefaultProfilePic() {
        Image pic = getPic("painting2.png");
        profilePic.setFill(new ImagePattern(pic));
        profilePic.setRadius(100);
    }

    public void uploadPicture(){
        notYetImplemented();
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