package com.example.javafx.controller;

import javafx.stage.Screen;

/**
 * Abstrakte Klasse für Verwendung von layoutrelevanten Werten zur Laufzeit.
 * Alle Component-Controller sollten diese Datei extenden. Im FXML können die Werte dann verwendet werden,
 * z.B. mit ${controller.height}.
 */
public abstract class PapaController {

    /**
     * erstellt Padding an den Seiten für Bildschirminhalt
     */
    public int getOffsetX() {
        return 70;
    }

    /**
     * Offset nach oben, damit Navbar & Banner noch Platz haben
     */
    public double getOffsetY() {
        return 140.00;
    }

    /**
     * Height für Bildschirminhalt. Berücksichtigt schon den Y Offset.
     */
    public double getHeight() {
        System.out.println(Screen.getPrimary().getVisualBounds().getHeight());
        return Screen.getPrimary().getVisualBounds().getHeight() - getOffsetY();
    }

    /**
     * Width für Bildschirminhalt. Berücksichtigt schon den X Offset.
     */
    public double getWidth() {
        System.out.println(Screen.getPrimary().getVisualBounds().getWidth());
        // * 2, weil links & rechts padding sein soll:
        // Durch den x Offset im fxml (layoutX=${controller.getXOffset}) gilt der schon links, durch das *2 wird die Width so weit
        // beschränkt, dass auch rechts Platz ist.
        return Screen.getPrimary().getVisualBounds().getWidth() - getOffsetX() * 2;
    }
}