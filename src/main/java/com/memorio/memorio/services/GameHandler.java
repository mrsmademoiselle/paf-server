package com.memorio.memorio.services;

import com.memorio.memorio.entities.Game;

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
        gameInstance.getBoard().flipCard(cardId);
    }

    public Game getGame() {
        return gameInstance;
    }

    public void setGame(Game game) {
        gameInstance = game;
    }
}