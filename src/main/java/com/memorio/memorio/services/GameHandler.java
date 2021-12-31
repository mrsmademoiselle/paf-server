package com.memorio.memorio.services;

import com.memorio.memorio.entities.Card;
import com.memorio.memorio.entities.FlipStatus;
import com.memorio.memorio.entities.Game;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Dient der Persistierung und Verarbeitung eines einzigen Game-Objekts zur Laufzeit.
 * Jedes Spiel hat somit seine eigene GameHandler Instanz.
 * <p>
 * TODO WebsocketServer und GameHandler auseinanderziehen
 */
public class GameHandler {
    private Game gameInstance;

    public GameHandler(Game game) {
        this.gameInstance = game;
    }

    public GameHandler() {
    }

    public void flipCard(String cardId) {
        List<Card> cardSet = gameInstance.getBoard().getCardSet();

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

    public Game getGame() {
        return gameInstance;
    }

    public void setGame(Game game) {
        gameInstance = game;
    }
}