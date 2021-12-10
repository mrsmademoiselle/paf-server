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
                //TODO: make things prettier
                setFeedback();

            }else{
                clearInfoLabel();
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
                    fillInfoLabel(false);
                }
            } else {
                // User Notification
                fillInfoLabel(false);
            }
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
            // Bild visuell setzen
            Image image = new Image(selectedFile.toURI().toString());
            ImagePattern imagePattern = new ImagePattern(image);
            profilePic.setFill(imagePattern);

            // file -> bytes
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

    private void fillInfoLabel(boolean successfulRegistration) {
        // Später muss der erfolgreich-Fall nicht mehr behandelt werden, stattdessen eine Weiterleitung auf Startseite
        String labelText = successfulRegistration ? "Der Benutzer wurde erfolgreich angelegt." : "Der Benutzer konnte nicht angelegt werden.";
        String labelBackground = successfulRegistration ? "#6dd06d" : "#d06d6d";
        String labelColor = successfulRegistration ? "#004100" : "#410000";
        bannerLabel.setText(labelText);
        bannerLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf(labelBackground), new CornerRadii(10), null)));
        bannerLabel.setTextFill(Paint.valueOf(labelColor));
        bannerLabel.setVisible(true);
    }

    private void setFeedback(){
        // TODO: Spaeter modularisieren erstmal nur fuer livefeedback beim tippen
        bannerLabel.setText("Es sind nur Buchstaben und Zahlen erlaubt!");
        bannerLabel.setBackground(new Background(new BackgroundFill(Paint.valueOf("#d06d6d"), new CornerRadii(10), null)));
        bannerLabel.setTextFill(Paint.valueOf("#410000"));
        bannerLabel.setVisible(true);
    }

    private void clearInfoLabel(){
        bannerLabel.setText("");
        bannerLabel.setStyle("");
        bannerLabel.setVisible(false);
    }

}