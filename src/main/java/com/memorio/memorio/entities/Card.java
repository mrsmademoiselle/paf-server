package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import java.util.UUID;

/**
 * Dieses Objekt repräsentiert eine einzige Memory-Karte mit deren Bild.
 */
@Getter
@ToString
@Setter
@Embeddable
public class Card {
    private String id;
    private int pairId;
    private boolean isFlipped;

    @Deprecated
    public Card() {
    }

    public Card(int pairID) {
        this.pairId = pairID;
        this.isFlipped = false;
        this.id = UUID.randomUUID().toString();
    }

    public void flipCard() {
        this.isFlipped = true;
    }
}