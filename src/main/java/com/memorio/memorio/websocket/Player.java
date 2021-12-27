package com.memorio.memorio.websocket;

import java.util.ArrayList;
import java.util.List;
import com.memorio.memorio.websocket.Match;
import org.java_websocket.WebSocket;
import java.util.UUID;

public class Player {
    /*
     *
     * Diese Klasse hält die zugehörige WebSocket
     * sowie eine Liste anderer Player (subscriber), zu denen alle 
     * eingehenden Nachrichten gesendet werden.
     *
     * TODO:
     * ebenfalls hier gespeichert ist das token. Das muss evtl. noch
     * angepasst werden und das eigentliche JWTToken enthalten.
     *
     */
    private WebSocket conn;
    private List<Player> subscribers = new ArrayList<>();
    private UUID token;
    private Match match = null;

    public Player(WebSocket conn){
	this.token = UUID.randomUUID();
	this.conn = conn;
    }

    public void setConnection(WebSocket conn){this.conn = conn;}
    public WebSocket getConnection(){return this.conn;}
    public void setMatch(Match m){this.match = m;}
    public Match getMatch(){return this.match;}
    public UUID getToken() {return this.token;}
    public List<Player> getSubscribers(){return this.subscribers;}
    
    public void addSubscriber(Player player){subscribers.add(player);}
    public void removeSubscriber(Player player){
	if(subscribers.contains(player)){subscribers.remove(player);}
    }

}

