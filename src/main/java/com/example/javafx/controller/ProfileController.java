package com.example.javafx.controller;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.UserService;
import com.example.javafx.service.helper.FileManager;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ProfileController extends PapaController {

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

    UserDto userDto;

    UserService userService = new UserService();


    @FXML
    public void initialize() {
        userDto = userService.getUserInfo();
        usernameTextfield.setText(userDto.getUsername());

        // TODO tatsächliches Bild vom Server nehmen
        setProfilePic();
        addInputListenersForTextfields();
    }

    public void setProfilePic() {
        // eigentlich sollte diese Zeile in unserem Fall nicht mehr auftreten, aber vorsichtshalber
        // lasse ich sie erstmal drin.
        if (userDto.getProfilePic() == null || userDto.getProfilePic().length == 0) {
            Image pic = FileManager.getPic("default.jpg");
            profilePic.setFill(new ImagePattern(pic));
            profilePic.setRadius(80);
        } else {
            Image img = new Image(new ByteArrayInputStream(userDto.getProfilePic()));

            /* Die Zeile war dafür da, dass beim Bildhochladen die Anwendung nicht crasht, weil es dort
            so lange Probleme beim Bild gab. Wir können es sicherheitshalber drin behalten, falls so etwas
            nochmal passiert, oder rausnehmen wenn wir uns sicher sind, *dass* es nicht nochmal passiert.
             */
            if (img.isError()) return;

            ImagePattern imagePattern = new ImagePattern(img);
            profilePic.setFill(imagePattern);
            profilePic.setRadius(80);
        }
    }

    public void updateUser() {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();
        byte[] profilePic = userDto.getProfilePic();


        if (!username.matches("\\w*") || username.isBlank()) {
            bannerController.setText("Das Username oder Passwortformat wird nicht akzeptiert.", false);
            return;
        }
        // erst einmal ohne Bild updaten, wie beim Register
        boolean successful = userService.updateUser(username, password, new byte[]{});

        // wenn userInfo-Update erfolgreich, Bild hinterherschicken
        if (successful) {
            if (profilePic != null && profilePic.length > 0) {
                // falls Bildupload nicht geklappt hat, zeige Fehlerbanner an
                // evtl TODO: Banner für Userinfo & Bild auseinanderziehen
                if (!userService.uploadImage(profilePic)) {
                    successful = false;
                }
            }
        }
        String bannerText = successful ? "Die Userinformationen wurden erfolgreich bearbeitet." : "Die Userinformationen konnten nicht bearbeitet werden.";
        bannerController.setText(bannerText, successful);
    }

    public void changePicture() throws IOException {
        File selectedFile = FileManager.openImageChooser(page);

        if (selectedFile != null) {
            // transformieren des Bildes in Byte
            userDto.setProfilePic(Files.readAllBytes(selectedFile.toPath()));
            setProfilePic();
        }
    }

    public void removePicture() {
        // Dto hat das neue Standardbild vom Server
        userDto = userService.removeImage();
        setProfilePic();
    }

    /**
     * Dient der Live-Validierung beim Tippen.
     */
    private void addInputListenersForTextfields() {
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
}