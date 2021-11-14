package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@Entity
public class Match {
    //TODO: Rework lobbycode als ID

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String lobbyCode;

    // TODO: Admin evtl. rauskicken, sodass alle alle Rechte haben
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User lobbyAdmin;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", nullable = false)
    private User amZug;

    // TODO: Richtige Annotation
    @Transient
    private Board board;

    // TODO: Richtige Annotation
    @Transient
    private Map<User, Integer> userScores;

    public Match() {
    }
}