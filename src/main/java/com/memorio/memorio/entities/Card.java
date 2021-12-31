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
    private FlipStatus flipStatus;

    @Deprecated
    public Card() {
    }

    public Card(int pairID) {
        this.pairId = pairID;
        this.flipStatus = FlipStatus.NOT_FLIPPED;
        this.id = UUID.randomUUID().toString();
    }

    public void waitToFlip() {
        this.flipStatus = FlipStatus.WAITING_TO_FLIP;
    }

    public void flipCard() {
        this.flipStatus = FlipStatus.FLIPPED;
    }

    public void unflipCard() {
        this.flipStatus = FlipStatus.NOT_FLIPPED;
    }
}