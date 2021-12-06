package com.memorio.memorio.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Ein Match stellt den Zustand eines laufenden Spiels dar.
 * Zur Laufzeit wird ein Match mit den benötigten Informationen erstellt.
 * Nach Abschluss des Spiels wird das Match in der Datenbank persistiert.
 * Außerdem wird das Match in den UserProfilen aller teilnehmenden Usern gespeichert.
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Entity
public class Match {
    //TODO: Rework lobbycode als ID

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long lobbyCode;

    @Transient
    private User currentTurn;

    /* Embedded/Embeddable benutzt man, wenn man ein Objekt in einem Objekt persistieren will,
    das selbst keine Tabelle in der Datenbank hat. */
    @Embedded
    private Board board;

    @ElementCollection
    private List<UserScore> userScores;

    @Deprecated
    public Match() {
    }

    public Match(Long lobbyCode, User amZug, Board board) {
        this.lobbyCode = lobbyCode;
        this.currentTurn = amZug;
        this.board = board;
    }
}