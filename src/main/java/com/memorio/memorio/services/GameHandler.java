package com.memorio.memorio.services;

import com.memorio.memorio.entities.FlipStatus;
import com.memorio.memorio.entities.Game;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.entities.UserScore;

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
        boolean foundMatchingPair = gameInstance.getBoard().flipCard(cardId);

        if (foundMatchingPair) {
            updateUserScore();
        } else {
            switchCurrentTurnIfNeeded();
        }

    }

    /**
     * Nach flipCard() sollte es maximal eine Karte geben, die den FlipStatus "Waiting to flip" hat.
     * Alle anderen Karten sind entweder aufgedeckt oder nicht.
     * Wenn es eine Karte gibt, dann handelt es sich bei diesem Zug um den ersten Zug, und der User am Zug wird nicht gewechselt.
     * Wenn es keine Karte mit dem FlipStatus "waiting to flip" gibt, dann wurde der Zug beendet und es muss gewechselt werden.
     */
    private void switchCurrentTurnIfNeeded() {
        boolean isFirstCard = this.gameInstance.getBoard().getCardSet().stream()
                .anyMatch(card -> card.getFlipStatus().equals(FlipStatus.WAITING_TO_FLIP));

        if (!isFirstCard) gameInstance.switchUser();
    }

    private void updateUserScore() {
        // update user score, wenn ein matchendes Kartenpaar gefunden wurde
        User currentTurn = gameInstance.getCurrentTurn();
        UserScore userScoreToUpdate = gameInstance.getUserScores().stream()
                .filter(userScore -> userScore.getUser().equals(currentTurn))
                .findFirst().orElseThrow(RuntimeException::new);
        userScoreToUpdate.increaseScore();

    }

    public Game getGame() {
        return gameInstance;
    }

    public void setGame(Game game) {
        gameInstance = game;
    }
}