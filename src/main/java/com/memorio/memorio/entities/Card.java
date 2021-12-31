package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Dieses Objekt repräsentiert eine einzige Memory-Karte mit deren Bild.
 */
@Getter
@ToString
@Setter
@Embeddable
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int pairId;

    private boolean isFlipped;

    @Deprecated
    public Card() {
    }

    public Card(int pairID) {
        this.pairId = pairID;
        this.isFlipped = false;
    }

    public void flipCard() {
        this.isFlipped = true;
    }

}