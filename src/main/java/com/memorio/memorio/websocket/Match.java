package com.memorio.memorio.websocket;

public class Match {
    private Player playerOne;
    private Player playerTwo;
    
    public Player getPlayerOne(){return this.playerOne;}
    public Player getPlayerTwo(){return this.playerTwo;}
    public void setPlayerOne(Player p){this.playerOne = p;}
    public void setPlayerTwo(Player p){this.playerTwo = p;}

    public Match(Player playerOne, Player playerTwo){
	this.playerOne = playerOne;
	this.playerTwo = playerTwo;
    }
}
