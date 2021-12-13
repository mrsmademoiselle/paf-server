package com.example.javafx.controller;

import javafx.stage.Screen;

public abstract class PapaController {

    /**
     * erstellt Padding an den Seiten für Bildschirminhalt
     */
    public double getXOffset() {
        return 70;
    }

    /**
     * Offset nach oben, damit Navbar & Banner noch Platz haben
     */
    public double getYOffset() {
        return 140;
    }

    /**
     * Height für Bildschirminhalt. Berücksichtigt schon den Y Offset.
     */
    public double getHeight() {
        System.out.println(Screen.getPrimary().getVisualBounds().getHeight());
        return Screen.getPrimary().getVisualBounds().getHeight() - getYOffset();
    }

    /**
     * Width für Bildschirminhalt. Berücksichtigt schon den X Offset.
     */
    public double getWidth() {
        System.out.println(Screen.getPrimary().getVisualBounds().getWidth());
        // * 2, weil links & rechts padding sein soll:
        // Durch den x Offset im fxml (layoutX=${controller.getXOffset}) gilt der schon links, durch das *2 wird die Width so weit
        // beschränkt, dass auch rechts Platz ist.
        return Screen.getPrimary().getVisualBounds().getWidth() - getXOffset() * 2;
    }
}