package com.example.javafx.controller;

import com.example.javafx.HttpConnector;
import com.example.javafx.model.UserAuthDto;
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
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;

import java.io.*;
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
    SceneController sceneController = SceneController.getInstance();


    @FXML
    protected void initialize() {
        setProfilePic();
        setButtonPic();

        // TODO: Werte müssen responsive gemacht werden (?)
        setFormLayout();
        // Werte haben keine Bedeutung, hat nur bei mir gepasst
        editProfilePic.setTranslateY(editProfilePic.getLayoutY() - 25);
        editProfilePic.setTranslateX(editProfilePic.getLayoutX() + 85);

        // Livevalidierung
        username.textProperty().addListener((obs, oldInput, newInput) -> {
            if (!username.getText().matches("[\\w|\\d]*")){
                username.setStyle("-fx-border-color:#d95252;"+
                        "-fx-border-width: 10;"
                );
                bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);
            }else{
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

    public void register(ActionEvent event) {
        if (username.getText().matches("[\\w|\\d]*") &&
                !password.getText().isBlank() && !username.getText().isBlank()) {

            UserAuthDto userAuthDto = new UserAuthDto(username.getText(), password.getText());

            // Userdaten registrieren
            boolean isOk = HttpConnector.post("user/register", userAuthDto);

            // Bild hochladen
            if(isOk && (imageBytes != null && imageBytes.length > 0)){
                // send to Dashboard
                boolean successfullyUploaded = HttpConnector.post("user/image/upload", imageBytes);
                if (successfullyUploaded ) {
                    sceneController.loadDashboard();
                } else {
                    bannerController.setText("Es gab einen Serverfehler beim verarbeiten des Bildes", false);
                }
            } else {
                // User Notification
                bannerController.setText("Es gab ein Problem mit dem Bild!", false);
            }
        }else{
            bannerController.setText("Es sind nur Buchstaben und Zahlen erlaubt", false);

        }
    }

    public void sendToLogin() {
        sceneController.loadLogin();
    }

    private void setButtonPic() {
        editProfilePic.setGraphic(new ImageView(getPic("edit.png")));
    }

    public void hoverOverProfilePic() {
        Image pic = getPic("upload.png");
        profilePic.setFill(new ImagePattern(pic));
        profilePic.setRadius(100);
        profilePic.setCursor(Cursor.HAND);
    }

    public void setProfilePic() {
        if (imageBytes == null || imageBytes.length == 0){
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

    private Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/pics");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        return null;
    }

}