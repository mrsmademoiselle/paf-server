package com.example.javafx.controller;

import com.example.javafx.service.UserService;
import com.example.javafx.service.helper.FileManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RegistrationController extends PapaController {

    // Bedeutet, dass dieses Feld in der fxml-Datei referenziert wird
    @FXML
    Button submit;

    @FXML
    TextField username;

    @FXML
    PasswordField password;

    @FXML
    Label loginLink;

    @FXML
    Circle profilePic;

    /* Einbindung der Navbar im FXML -> nicht löschen */
    @FXML
    NavbarController navbarController;

    @FXML
    Pane page;

    @FXML
    VBox form;

    @FXML
    BannerController bannerController;

    private byte[] imageBytes;

    UserService userService = new UserService();


    @FXML
    protected void initialize() {
        setProfilePic();
        activateInputListener();
    }


    public void register(ActionEvent event) {
        String username = this.username.getText();
        String password = this.password.getText();

        if (!username.matches("[\\w|\\d]*") ||
                password.isBlank() || username.isBlank()) {
            bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            return;
        }
        boolean successful = userService.registerUser(username, password);

        if (successful) {
            if (imageBytes != null && imageBytes.length > 0) {
                userService.uploadImage(imageBytes);
                // diesen Banner erreichen wir nur, wenn kein Bild hochgeladen werden konnte
                bannerController.setText("Es gab einen Serverfehler beim Verarbeiten des Bildes", false);

            } else {
                // User Notification
                userService.redirectToAccount();
            }
        } else {
            bannerController.setText("Der Benutzer konnte nicht angelegt werden.", false);
        }
    }

    public void sendToLogin() {
        userService.redirectToLogin();
    }

    public void hoverOverProfilePic() {
        Image pic = FileManager.getPic("button_upload.png");
        profilePic.setFill(new ImagePattern(pic));
        profilePic.setRadius(100);
        profilePic.setCursor(Cursor.HAND);
    }

    public void setProfilePic() {
        if (imageBytes == null || imageBytes.length == 0) {
            Image pic = FileManager.getPic("standard_profile_pic.png");
            profilePic.setFill(new ImagePattern(pic));
            profilePic.setStrokeWidth(3);
            profilePic.setStroke(Color.WHITE);
            profilePic.setRadius(100);
        } else {
            Image img = new Image(new ByteArrayInputStream(imageBytes));
            ImagePattern imagePattern = new ImagePattern(img);
            profilePic.setFill(imagePattern);
        }
    }

    public void uploadPicture() throws IOException {
        File selectedFile = FileManager.openImageChooser(page);

        if (selectedFile != null) {
            // Bild als Vorschau setzten
            Image image = new Image(selectedFile.toURI().toString());
            ImagePattern imagePattern = new ImagePattern(image);
            profilePic.setFill(imagePattern);

            // transformieren des Bildes in Byte
            imageBytes = Files.readAllBytes(selectedFile.toPath());
        }
    }


    private void activateInputListener() {
        // Livevalidierung
        username.textProperty().addListener((obs, oldInput, newInput) -> {
            if (!username.getText().matches("[\\w|\\d]*")) {
                username.setStyle("-fx-border-color:#d95252; -fx-border-width: 3;"
                );
                bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            } else {
                username.setStyle("");
            }
        });
    }


}