package com.memorio.memorio.entities;

import com.memorio.memorio.exceptions.MemorioRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
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
    // cascade.all = Anwenden aller primären Cascade-Typen, siehe:
    // https://openjpa.apache.org/builds/2.4.0/apache-openjpa/docs/jpa_overview_meta_field.html#jpa_overview_meta_cascade
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<UserScore> userScores;


    @Deprecated
    public Game() {
    }

    /**
     * Erstellt ein Game-Objekt anhand der Parameter.
     * User1 ist der User, der zuerst ziehen darf.
     */
    public Game(Board board, User user1, User user2) {
        this.board = board;
        this.currentTurn = user1;

        UserScore userScore1 = new UserScore(user1);
        UserScore userScore2 = new UserScore(user2);

        this.userScores = Arrays.asList(userScore1, userScore2);
    }

    /**
     * Zusammengesetztes Gameobjekt
     * @param lobbyCode Lobbycode des Spiels
     * @param board Zugehoeriges Board zum Spiel
     * @param userscore1 Erster User
     * @param userScore2 Zweiter User
     * @param currentTurn User der gerade dran ist
     */
    public Game(long lobbyCode, Board board, UserScore userscore1, UserScore userScore2, User currentTurn) {
        this.lobbyCode = lobbyCode;
        this.board = board;
        this.currentTurn = currentTurn;
        this.userScores = Arrays.asList(userscore1, userScore2);
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

    /**
     * Erstellt ein neues Game-Objekt mit den Werten wie dieses Objekt.
     */
    public Game copy() {
        if (userScores.size() != 2) throw new MemorioRuntimeException("Es müssen genau 2 Userscores vorhanden sein");

        // jede Karte muss neu erstellt werden, damit wir nicht ausversehen dieselben OBjekte (wgeen Speicherreferenzen) aktualisieren
        List<Card> cardList = new ArrayList<>();
        this.getBoard().getCardSet().forEach(card -> cardList.add(Card.createCard(card.getId(), card.getPairId(), card.getFlipStatus())));

        Game game = new Game();
        // Board muss neu erstellt werden
        Board board = Board.createBoard(cardList);
        game.setBoard(board);
        game.setCurrentTurn(getCurrentTurn());
        game.setLobbyCode(lobbyCode);
        // UserScores können gleich bleiben, weil wir diese für die SPielkopie nicht anfassen
        game.setUserScores(userScores);
        return game;
    }
}