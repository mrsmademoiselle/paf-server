package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Das Board repräsentiert das fertige Spielbrett im Spiel.
 * Es enthält ein Kartenset und die Anzahl der Tiles des Spielbretts.
 */
@ToString
@Getter
@Setter
@Embeddable
public class Board {

    @ElementCollection
    private List<Card> cardSet;

    public Board() {
        this.cardSet = createCardSet();
    }

    private List<Card> createCardSet() {
        List<Card> cardset = new ArrayList<>();
        for (int i = 2; i <= 17; i++) {
            int pairId = i / 2;
            System.out.println(pairId);
            cardset.add(new Card(pairId));
        }
        Collections.shuffle(cardset);

        return cardset;
    }

}