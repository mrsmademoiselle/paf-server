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

    /**
     * Matchobjekt
     * @param playerOne Erster User
     * @param playerTwo Zweiter User
     */
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

    /**
     * Gibt beide Spieler zurueck
     * @return
     */
    public List<Player> getBothPlayers() {
        return Arrays.asList(playerOne, playerTwo);
    }
}