package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
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

    public Board(List<Card> cardSet) {
        this.cardSet = cardSet;
    }

    @Deprecated
    public Board() {

    }

}