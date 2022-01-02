package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
            cardset.add(new Card(pairId));
        }
        Collections.shuffle(cardset);

        return cardset;
    }

    /**
     * Ist dafür zuständig, den Flipstatus einer Karte beim Umdrehen zu setzen.
     * Dafür wird geprüft, ob die Karte mit anderen, umgedrehten Karten matcht.
     */
    public void flipCard(String cardId) {
        List<Card> cardSet = getCardSet();

        // hole die Karte, die geflippt werden soll
        Card currentCard = cardSet.stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst().orElse(null);

        if (currentCard == null) return;

        // hole alle Karten, die bereits vom User in dieser Runde aufgedeckt wurden
        List<Card> allCardsWaitingToBeFlipped = cardSet.stream()
                .filter(card -> card.getFlipStatus().equals(FlipStatus.WAITING_TO_FLIP))
                .collect(Collectors.toList());

        // Wenn es eine Karte gibt die auf das flippen wartet und sie mit unserer Karte matcht, flippe beide
        if (allCardsWaitingToBeFlipped.size() == 1) {
            Card cardWaitingToBeFlipped = allCardsWaitingToBeFlipped.get(0);
            if (cardWaitingToBeFlipped.getPairId() == currentCard.getPairId()) {
                currentCard.flipCard();
                cardWaitingToBeFlipped.flipCard();
            } else { // ansonsten unflippe beide
                currentCard.unflipCard();
                cardWaitingToBeFlipped.unflipCard();
            }
            // Wenn es keine Karten gibt die auf das flippen warten, bringe currentCard in den Wartemodus bis zum nächsten Kartenzug
        } else if (allCardsWaitingToBeFlipped.size() == 0) {
            currentCard.waitToFlip();
            // wenn mehr als 1 Karte wartet (darf eigentlich nicht passieren),
            // dann ist was schiefgegangen und wir unflippen alle erstmal
        } else {
            allCardsWaitingToBeFlipped.forEach(Card::unflipCard);
        }
    }
}