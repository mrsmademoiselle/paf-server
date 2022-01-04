package com.example.javafx.model;

import com.example.javafx.model.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Das Board repräsentiert das fertige Spielbrett im Spiel.
 * Es enthält ein Kartenset und die Anzahl der Tiles des Spielbretts.
 */

public class Board {

    private List<Card> cardSet;

    public Board() {
        this.cardSet = createCardSet();
    }

    private List<Card> createCardSet() {
        List<Card> cardset = new ArrayList<>();
        for (int i = 2; i <= 17; i++) {
            int pairId = i / 2;
            cardset.add(new Card());
        }
        Collections.shuffle(cardset);

        return cardset;
    }

}