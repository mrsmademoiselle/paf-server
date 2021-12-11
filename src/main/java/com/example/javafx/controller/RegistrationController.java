package com.example.javafx.controller;

import com.example.javafx.service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RegistrationController {

    // Bedeutet, dass dieses Feld in der fxml-Datei referenziert wird
    @FXML
    Button submit;

    @FXML
    Button editProfilePic;

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
    Label title;

    @FXML
    AnchorPane page;

    @FXML
    VBox form;

    @FXML
    VBox successOrErrorContainer;

    @FXML
    Label bannerLabel;

    @FXML
    BannerController bannerController;

    private byte[] imageBytes;

    private static double applicationWidth;
    private static double applicationHeight;
    UserService userService = new UserService();


    @FXML
    protected void initialize() {
        setProfilePic();
        setButtonPic();

        // TODO: Werte müssen responsive gemacht werden (?)
        setFormLayout();
        // Werte haben keine Bedeutung, hat nur bei mir gepasst
        editProfilePic.setTranslateY(editProfilePic.getLayoutY() - 25);
        editProfilePic.setTranslateX(editProfilePic.getLayoutX() + 85);

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
        boolean successful = userService.registerUserData(username, password);

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
        Image pic = getPic("upload.png");
        profilePic.setFill(new ImagePattern(pic));
        profilePic.setRadius(100);
        profilePic.setCursor(Cursor.HAND);
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

    private void activateInputListener() {
        // Livevalidierung
        username.textProperty().addListener((obs, oldInput, newInput) -> {
            if (!username.getText().matches("[\\w|\\d]*")) {
                username.setStyle("-fx-border-color:#d95252;" +
                        "-fx-border-width: 3;"
                );
                bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            } else {
                username.setStyle("");
            }
        });
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

    private void setButtonPic() {
        editProfilePic.setGraphic(new ImageView(getPic("edit.svg")));
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