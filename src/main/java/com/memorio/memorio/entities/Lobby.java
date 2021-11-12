package com.memorio.memorio.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby {

    private List<User> users;
    private String lobbyCode;

    public Lobby() {
        this.users = new ArrayList<>();
    }

    public void removeParticipant(User user) {
        users.remove(user);
    }

    public void addParticipant(User user) {
        this.users.add(user);
    }

    public void generateLobbyCode() {
        this.lobbyCode = UUID.randomUUID().toString();
    }

    public void createMatch() {
        //TODO: Matchcreation
    }
}