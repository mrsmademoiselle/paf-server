package com.memorio.memorio.websocket;

import java.util.ArrayList;
import java.util.List;

import com.memorio.memorio.entities.User;
import org.java_websocket.WebSocket;

import java.util.Optional;
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
    private String token;
    private Match match = null;
    // Verheiraten von Player und User
    private User user;

    /**
     * Playekonstruktor
     * @param conn Websocketverbindung des Spieles
     * @param user Userpobjekt mit dem der Player verheiratet werden soll
     */
    public Player(WebSocket conn, Optional<User> user, String jwt){
        this.token = jwt;
        this.conn = conn;
    }

    public void setConnection(WebSocket conn){this.conn = conn;}
    public WebSocket getConnection(){return this.conn;}
    public void setMatch(Match m){this.match = m;}
    public Match getMatch(){return this.match;}
    public String getToken() {return this.token;}
    public User getUser(){return this.user;}
    public List<Player> getSubscribers(){return this.subscribers;}
    
    public void addSubscriber(Player player){subscribers.add(player);}
    public void removeSubscriber(Player player){
        if(subscribers.contains(player)){subscribers.remove(player);}
    }

    /**
     * Entfernen der User - Spieler verbindung fuer Spielende
     */
    public void clearUser(){
        this.user = null;
    }

}

