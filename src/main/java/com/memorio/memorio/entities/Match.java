package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Map;

@Getter
@Setter
public class Match {
    //TODO: Rework lobbycode als ID

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String lobbyCode;
    // TODO: Admin evtl. rauskicken, sodass alle alle Rechte haben
    private User lobbyAdmin;
    private User amZug;
    private Board board;
    private Map<User, Integer> userScores;

    public Match() {
    }
}