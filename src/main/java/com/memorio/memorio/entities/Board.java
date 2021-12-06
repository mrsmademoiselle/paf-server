package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

/**
 * Das Board repräsentiert das fertige Spielbrett im Spiel.
 * Es enthält ein Kartenset und die Anzahl der Tiles des Spielbretts.
 */
@ToString
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

    @Deprecated
    public Board() {

    }
}