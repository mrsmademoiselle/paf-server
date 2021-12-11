package com.example.javafx.controller;

import com.example.javafx.model.UserDto;
import com.example.javafx.service.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AccountDataController {

    @FXML
    AnchorPane page;

    @FXML
    Circle profilePic;

    @FXML
    TextField usernameTextfield;

    @FXML
    PasswordField passwordTextfield;

    @FXML
    BannerController bannerController;

    byte[] imageBytes;
    UserService userService = new UserService();

    @FXML
    public void initialize() {
        UserDto userDto = userService.getUserInfo();
        usernameTextfield.setText(userDto.getUsername());
        // TODO tatsächliches Bild vom Server nehmen
        setProfilePic();
    }

    public void setProfilePic() {
        if (imageBytes == null || imageBytes.length == 0) {
            Image pic = getPic("painting2.png");
            profilePic.setFill(new ImagePattern(pic));
            profilePic.setRadius(100);
        } else {
            Image img = new Image(new ByteArrayInputStream(imageBytes));
            ImagePattern imagePattern = new ImagePattern(img);
            profilePic.setFill(imagePattern);
        }
    }

    public void updateUserInfo(MouseEvent mouseEvent) {
        String username = usernameTextfield.getText();
        String password = passwordTextfield.getText();

        if (!username.matches("[\\w|\\d]*") || username.isBlank() || password.isBlank()) {
            bannerController.setText("Der Username darf nur aus Buchstaben und Zahlen bestehen.", false);
            return;
        }
        boolean successful = userService.updateUserInfo(username, password, imageBytes);

        String bannerText = successful ? "Die Userinformationen wurden erfolgreich bearbeitet." : "Die Userinformationen konnten nicht bearbeitet werden.";
        bannerController.setText(bannerText, successful);
    }

    public void uploadPicture() throws IOException {
        // File Chooser für Bildauswahl
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Profilbild hochladen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(page.getScene().getWindow());

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

    private Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/pics");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        return null;
    }

}