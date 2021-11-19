package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

/**
 * Das Board repräsentiert das fertige Spielbrett im Spiel.
 * Es enthält ein Kartenset und die Anzahl der Tiles des Spielbretts.
 */
@Getter
@Setter
@Embeddable
public class Board {

    private int tileCount;
    @OneToOne
    private CardSet cardSet;

    public Board(int boardSize, CardSet cardSet) {
        this.tileCount = boardSize;
        this.cardSet = cardSet;
    }

    public Board() {

    }
}