package com.memorio.memorio.entities;

import com.memorio.memorio.valueobjects.FlipStatus;
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

    /**
     * Karte
     * @param pairID Die PairID der Karte
     */
    public Card(int pairID) {
        this.pairId = pairID;
        this.flipStatus = FlipStatus.NOT_FLIPPED;
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Factory-Methode die eine Karte mit den reingegebenen Werten erstellt
     */
    public static Card createCard(String id, int paiId, FlipStatus flipStatus) {
        Card card = new Card();
        card.setId(id);
        card.setPairId(paiId);
        card.setFlipStatus(flipStatus);
        return card;
    }

    /**
     * Setze Flipstatus auf WAITING_TO_FLIP
     */
    public void waitToFlip() {
        this.flipStatus = FlipStatus.WAITING_TO_FLIP;
    }

    /**
     * Setze Flipstatus auf FLIPPED
     */
    public void flipUp() {
        this.flipStatus = FlipStatus.FLIPPED;
    }

    /**
     * Setze Flipstatus auf NOT_FLIPPED
     */
    public void flipDown() {
        this.flipStatus = FlipStatus.NOT_FLIPPED;
    }
}