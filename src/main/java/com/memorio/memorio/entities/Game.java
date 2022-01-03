package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;

/**
 * Ein Game stellt den Zustand eines laufenden Spiels dar.
 * Zur Laufzeit wird ein Game mit den benötigten Informationen erstellt.
 * Nach Abschluss des Spiels wird das Game in der Datenbank persistiert.
 * Außerdem wird das Game in den UserProfilen aller teilnehmenden Usern gespeichert.
 */
@ToString
@Getter
@Setter
@Entity
public class Game {
    /*TODO: Rework komplett und verschmelzen mit Game aus Websocket
     */

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
    public Game() {
    }

    public Game(User amZug, Board board, User currentTurn, User user1, User user2) {
        this.currentTurn = amZug;
        this.board = board;
        this.currentTurn = currentTurn;

        UserScore userScore1 = new UserScore(user1);
        UserScore userScore2 = new UserScore(user2);

        this.userScores = Arrays.asList(userScore1, userScore2);
    }

    /**
     * Setzt den User als "User am Zug", der als letztes *nicht* am Zug war.
     */
    public void switchUser() {
        User jetztAmZug = this.getUserScores().stream()
                .filter(userScore -> !userScore.getUser().equals(currentTurn))
                // es gibt immer mindestens 2 User, die Bedingung muss also immer etwas zurückliefern, es sei denn es gibt große Probleme
                .findFirst().get().getUser();

        setCurrentTurn(jetztAmZug);
    }
}