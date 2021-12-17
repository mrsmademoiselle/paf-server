package com.example.javafx.controller;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.UserService;
import com.example.javafx.service.helper.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AccountDataController extends PapaController {

    @FXML
    Pane page;

    @FXML
    Circle profilePic;

    @FXML
    TextField usernameTextfield;

    @FXML
    PasswordField passwordTextfield;

    @FXML
    BannerController bannerController;

    @FXML
    NavbarController navbarController;

    byte[] imageBytes;
    UserService userService = new UserService();


    @FXML
    public void initialize() {
        UserDto userDto = userService.getUserInfo();
        usernameTextfield.setText(userDto.getUsername());

        // TODO tatsächliches Bild vom Server nehmen
        setProfilePic();
        activateInputListener();
    }

    public void setProfilePic() {
        if (imageBytes == null || imageBytes.length == 0) {
            Image pic = FileManager.getPic("standard_profile_pic.png");
            profilePic.setFill(new ImagePattern(pic));
            profilePic.setRadius(80);
        } else {
            Image img = new Image(new ByteArrayInputStream(imageBytes));
            ImagePattern imagePattern = new ImagePattern(img);
            profilePic.setFill(imagePattern);
        }
    }

    public void updateUserInfo(MouseEvent mouseEvent) {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        if (!username.matches("\\w*") || username.isBlank()) {
            bannerController.setText("Das Username oder Passwortformat wird nicht akzeptiert.", false);
            return;
        }
        boolean successful = userService.updateUserInfo(username, password, imageBytes);

        String bannerText = successful ? "Die Userinformationen wurden erfolgreich bearbeitet." : "Die Userinformationen konnten nicht bearbeitet werden.";
        bannerController.setText(bannerText, successful);
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

    public void removePicture(MouseEvent mouseEvent) {
        imageBytes = new byte[]{};
        setProfilePic();
    }

    private void activateInputListener() {
        // Livevalidierung
        usernameTextfield.textProperty().addListener((obs, oldInput, newInput) -> {
            if (!usernameTextfield.getText().matches("[\\w|\\d]*")) {
                usernameTextfield.setStyle("-fx-border-color:#d95252;" +
                        "-fx-border-width: 3;"
                );
                bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            } else {
                usernameTextfield.setStyle("");
            }
        });
    }

    public void login(MouseEvent mouseEvent) {
        userService.redirectToLogin();
        ;
    }
}