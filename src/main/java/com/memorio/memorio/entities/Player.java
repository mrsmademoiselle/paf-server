package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse hält die zugehörige WebSocket
 * sowie eine Liste anderer Player (subscriber), zu denen alle
 * eingehenden Nachrichten gesendet werden.
 * <p>
 * TODO:
 * ebenfalls hier gespeichert ist das token. Das muss evtl. noch
 * angepasst werden und das eigentliche JWTToken enthalten.
 */
@Getter
@Setter
public class Player {

    private WebSocket websocketConnection;
    private List<Player> subscribers = new ArrayList<>();
    private String token;
    private Match match = null;
    // Verheiraten von Player und User
    private User user;

    /**
     * Playerkonstruktor
     *
     * @param conn Websocketverbindung des Spieles
     * @param user Userpobjekt mit dem der Player verheiratet werden soll
     */
    public Player(WebSocket conn, User user, String jwt) {
        this.user = user;
        this.token = jwt;
        this.websocketConnection = conn;
    }

    public void addSubscriber(Player player) {
        subscribers.add(player);
    }

    public void removeSubscriber(Player player) {
        if (subscribers.contains(player)) {
            subscribers.remove(player);
        }
    }

    /**
     * Entfernen der User - Spieler verbindung fuer Spielende
     */
    public void clearUser() {
        this.user = null;
    }

}