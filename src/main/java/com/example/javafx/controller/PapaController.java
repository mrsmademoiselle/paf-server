package com.example.javafx.controller;

import javafx.stage.Screen;

/**
 * Abstrakte Klasse für Verwendung von layoutrelevanten Werten zur Laufzeit.
 * Alle Component-Controller sollten diese Datei extenden. Im FXML können die Werte dann verwendet werden,
 * z.B. mit ${controller.height}.
 * <p>
 * <p>
 * PS:
 * Falls ihr einen "cannot bind StackPane.layoutX"-Fehler bekommt beim Verwenden dieser Felder für
 * layoutX/layoutY, liegt das an der StackPane. StackPanes (und ein paar andere Komponenten wie z.B. AnchorPanes)
 * legen das Layout ihrer Kinder automatisch fest. Wenn man entsprechend versucht das manuell zu setzen, kommt dieser
 * Fehler, weil die Property ja schon "gebindet" ist. Das kann man entweder umgehen indem man stattdessen "Pane" verwendet,
 * oder indem man der StackPane die Property "managed=false" gibt.
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
        return 150.00;
    }

    /**
     * Height für Bildschirminhalt. Berücksichtigt schon den Y Offset.
     */
    public double getHeightWithOffset() {
        return Screen.getPrimary().getVisualBounds().getHeight() - getOffsetY();
    }

    /**
     * Width für Bildschirminhalt. Berücksichtigt schon den X Offset.
     */
    public double getWidthWithOffset() {
        // * 2, weil links & rechts padding sein soll:
        // Durch den x Offset im fxml (layoutX=${controller.getXOffset}) gilt der schon links, durch das *2 wird die Width so weit
        // beschränkt, dass auch rechts Platz ist.
        return Screen.getPrimary().getVisualBounds().getWidth() - getOffsetX() * 2;
    }

    /**
     * Width für Komponenten wie Banner & Navbar, die die komplette Breite (ohne Padding) einnehmen sollen.
     */
    public double getWidthWithoutOffset() {
        return Screen.getPrimary().getVisualBounds().getWidth();

    }
}