package com.example.javafx.service.helper;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;

public class FileManager {

    public static File openImageChooser(Pane page) {
        // File Chooser für Bildauswahl
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Profilbild hochladen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(page.getScene().getWindow());
        return selectedFile;
    }

    public static Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/pics");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        throw new RuntimeException("RegistrationController: Bild konnte nicht geladen werden: " + fileName);
    }
}