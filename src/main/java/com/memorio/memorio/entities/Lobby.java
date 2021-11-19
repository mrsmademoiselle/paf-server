package com.memorio.memorio.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Eine Lobby wird vor Beginn des Spiels erstellt.
 * Um mehrere User einladen zu können, wird ein LobbyCode generiert.
 * Später wird die Lobby zum Match konvertiert, und der LobbyCode wird zur MatchId.
 */
public class Lobby {

    private List<User> participant;
    private String lobbyCode;

    public Lobby() {
        this.participant = new ArrayList<>();
        generateLobbyCode();
    }

    public void removeParticipant(User user) {
        participant.remove(user);
    }

    public void addParticipant(User user) {
        this.participant.add(user);
    }

    public void generateLobbyCode() {
        this.lobbyCode = UUID.randomUUID().toString();
    }

    public void createMatch() {
        //TODO: Matchcreation
    }
}