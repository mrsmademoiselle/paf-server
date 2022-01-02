package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

/**
 * Diese Klasse enthält zwei Player-Instanzen die erfolgreich gematched wurden.
 */
@ToString
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

    public List<Player> getBothPlayers() {
        return Arrays.asList(playerOne, playerTwo);
    }
}