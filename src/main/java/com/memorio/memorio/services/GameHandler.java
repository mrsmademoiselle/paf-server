package com.memorio.memorio.services;

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

        // update user score, wenn ein matchendes Kartenpaar gefunden wurde
        if (foundMatchingPair) {
            User currentTurn = gameInstance.getCurrentTurn();
            UserScore userScoreToUpdate = gameInstance.getUserScores().stream()
                    .filter(userScore -> userScore.getUser().equals(currentTurn))
                    .findFirst().orElseThrow(RuntimeException::new);
            userScoreToUpdate.increaseScore();
        }
    }

    public Game getGame() {
        return gameInstance;
    }

    public void setGame(Game game) {
        gameInstance = game;
    }
}