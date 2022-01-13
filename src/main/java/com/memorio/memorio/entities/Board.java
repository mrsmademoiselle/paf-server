package com.memorio.memorio.entities;

import com.memorio.memorio.valueobjects.FlipStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
public class Board {

    private List<Card> cardSet;
    private List<String> temporarilyFlippedCards = new ArrayList<>();

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
     * <p>
     * Returnt true wenn ein matchendes Kartenpaar gefunden wurde, ansonsten false.
     */
    public boolean flipCard(String cardId) {
        // Entferne alle Elemente, die beim letzten Zug temporär aufgedeckt werden sollten.
        temporarilyFlippedCards.clear();
        List<Card> cardSet = getCardSet();

        // finde Karte, die geflippt werden soll
        Card currentCard = cardSet.stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst().orElse(null);

        // wenn wir unsere Karte nicht finden, brich ab. Dann darf der Nutzer nochmal ziehen.
        if (currentCard == null) return false;

        // hole alle Karten, die bereits vom User in dieser Runde aufgedeckt wurden
        List<Card> allCardsWaitingToBeFlipped = cardSet.stream()
                .filter(card -> card.getFlipStatus().equals(FlipStatus.WAITING_TO_FLIP))
                .collect(Collectors.toList());

        return handleCardFlipping(currentCard, allCardsWaitingToBeFlipped);
    }

    /**
     * Evaluiert, ob und wann welche Karten umgedreht werden sollen und flippt die Karten entsprechend.
     * Gibt boolean zurück, der angibt, ob ein matching-Pair gefunden wurde oder nicht.
     */
    private boolean handleCardFlipping(Card currentCard, List<Card> allCardsWaitingToBeFlipped) {
        // Wenn es keine Karten gibt die auf das flippen warten (= erster Zug), bringe unsere Karte in
        // den Wartemodus bis zum nächsten Kartenzug
        if (allCardsWaitingToBeFlipped.size() == 0) {
            currentCard.waitToFlip();

            return false;
        } else if (allCardsWaitingToBeFlipped.size() == 1) {
            Card cardWaitingToBeFlipped = allCardsWaitingToBeFlipped.get(0);

            // Wenn es eine Karte gibt die auf das flippen wartet und sie mit unserer Karte matcht, flippe beide
            if (cardWaitingToBeFlipped.getPairId() == currentCard.getPairId()) {
                currentCard.flipUp();
                cardWaitingToBeFlipped.flipUp();

                return true;
            } else { // ansonsten unflippe beide
                currentCard.flipDown();
                cardWaitingToBeFlipped.flipDown();
                // füge sie zur Liste hinzu, damit die Clients sie beide temporär aufdecken können
                temporarilyFlippedCards.add(currentCard.getId());
                temporarilyFlippedCards.add(cardWaitingToBeFlipped.getId());

                return false;
            }
        } else {
            // wenn mehr als 1 Karte wartet (darf eigentlich nicht passieren),
            // dann ist was schiefgegangen und wir unflippen alle erstmal

            allCardsWaitingToBeFlipped.forEach(Card::flipDown);
            return false;
        }
    }
}