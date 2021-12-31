package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

/**
 * Diese Klasse enthält zwei Player-Instanzen die erfolgreich gematched wurden.
 */
@Getter
@Setter
public class Match {

    private Player playerOne;
    private Player playerTwo;

    public Match(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    /**
     * Entfernen der Player aus dem Game für Spielende
     */
    public void removeAllPlayers() {
        this.playerOne = null;
        this.playerTwo = null;
    }
}