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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long lobbyCode;
    @Transient
    private User currentTurn;
    @Transient
    private Board board;

    /**
     * Dieses Feld darf kein Embeddable sein wie ursprünglich angedacht, weil es eine Collection von
     * Usern enthält und User eine DB-Entity ist. Daher ist UserScore jetzt bei uns (der Einfachheit halber) auch
     * eine Entität in der Datenbank und wird erst einmal separat abgespeichert. Ich versuche, das später noch einmal
     * zu fixen.
     * <p>
     * https://stackoverflow.com/questions/22126397/embeddable-and-elementcollection-nesting
     */
    // FetchType.Eager muss sein, damit die Collection zum Zeitpunkt der Initialisierung da ist
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<UserScore> userScores;


    @Deprecated
    public Game() {
    }

    public Game(Board board, User currentTurn, User user1, User user2) {
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