package com.example.javafx.service.helper;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Hilfsklasse fuer Ressourcenzugriffe
 */
public class FileManager {

    /**
     * Bildauswahl oeffnen
     */
    public static File openImageChooser(Pane page) {
        // File Chooser für Bildauswahl
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Profilbild hochladen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        return fileChooser.showOpenDialog(page.getScene().getWindow());
    }

    /**
     * Laden von Bildern aus Ressources
     *
     * @param fileName Name des zu ladenden Bildes
     * @return Bild das geladen wird
     */
    public static Image getPic(String fileName) {
        File folder = new File("src/main/resources/com/example/javafx/images");
        if (folder.exists()) {
            File imageFile = new File(folder, fileName);

            return new Image(imageFile.toURI().toString());
        }
        throw new RuntimeException("RegistrationController: Bild konnte nicht geladen werden: " + fileName);
    }
}