package com.memorio.memorio.websocket;

public class Match {
    /*
     *
     * Diese Klasse enthält zwei Player-Instanzen die 
     * erfolgreich gematched wurden.
     *
     */
    private Player playerOne;
    private Player playerTwo;

    /**
     * Matchkonstruktur
     * @param playerOne Spieler eins
     * @param playerTwo Spieler zwei
     */
    public Match(Player playerOne, Player playerTwo){
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }
    
    public Player getPlayerOne(){return this.playerOne;}
    public Player getPlayerTwo(){return this.playerTwo;}
    public void setPlayerOne(Player p){this.playerOne = p;}
    public void setPlayerTwo(Player p){this.playerTwo = p;}

    /**
     * Entfernen der Player aus dem Match fuer Spielende
     */
    public void removePlayer(){
        this.playerOne = null;
        this.playerTwo = null;
    }
}
