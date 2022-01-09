package com.memorio.memorio.services.helper;

import com.memorio.memorio.entities.Game;
import com.memorio.memorio.entities.User;
import com.memorio.memorio.entities.UserScore;
import com.memorio.memorio.exceptions.MemorioRuntimeException;
import com.memorio.memorio.valueobjects.FlipStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dient der Persistierung und Verarbeitung eines einzigen Game-Objekts zur Laufzeit.
 * Jedes Spiel hat somit seine eigene GameHandler Instanz.
 * <p>
 * TODO WebsocketServer und GameHandler auseinanderziehen
 */
public class GameHandler {
    private final Logger logger = LoggerFactory.getLogger(GameHandler.class);
    private Game gameInstance;

    public GameHandler(Game game) {
        this.gameInstance = game;
    }

    public GameHandler() {
    }

    /**
     * Flippt die Karten entsprechend ihres Flipstatus'. Updated den UserScore wenn notwendig.
     * Ändert den Spieler-Am-Zug wenn notwendig.
     * <p>
     * Gibt boolean zurück der aussagt, ob es noch weitere Karten gibt, die gezogen werden können, oder nicht.
     * Wenn dieser boolean false ist, kann das Spiel beendet werden.
     */
    public boolean flipCard(String cardId) {
        boolean foundMatchingPair = gameInstance.getBoard().flipCard(cardId);

        if (foundMatchingPair) {
            updateUserScore();
        } else {
            switchCurrentTurnIfNeeded();
        }

        return hasAnyUnflippedCardsLeft();

    }

    /**
     * Evaluiert, ob es irgendwelche Karten gibt, die noch nicht umgedreht wurden.
     */
    private boolean hasAnyUnflippedCardsLeft() {
        boolean hasAnyUnflippedCards = gameInstance.getBoard().getCardSet().stream()
                .anyMatch(card -> card.getFlipStatus().equals(FlipStatus.NOT_FLIPPED));

        return hasAnyUnflippedCards;
    }

    /**
     * Nach flipCard() sollte es maximal eine Karte geben, die den FlipStatus "Waiting to flip" hat.
     * Alle anderen Karten sind entweder aufgedeckt oder nicht.
     * Wenn es eine Karte gibt, dann handelt es sich bei diesem Zug um den ersten Zug, und der User am Zug wird nicht gewechselt.
     * Wenn es keine Karte mit dem FlipStatus "waiting to flip" gibt, dann wurde der Zug beendet und es muss gewechselt werden.
     */
    private void switchCurrentTurnIfNeeded() {
        boolean isFirstCard = this.gameInstance.getBoard().getCardSet().stream()
                .anyMatch(card -> card.getFlipStatus().equals(FlipStatus.WAITING_TO_FLIP));

        if (!isFirstCard) gameInstance.switchUser();
    }

    /**
     * Erhöht den Userscore des Users, der gerade am Zug ist.
     */
    private void updateUserScore() {
        // update user score, wenn ein matchendes Kartenpaar gefunden wurde
        User currentTurn = gameInstance.getCurrentTurn();
        UserScore userScoreToUpdate = gameInstance.getUserScores().stream()
                .filter(userScore -> userScore.getUser().equals(currentTurn))
                .findFirst().orElseThrow(MemorioRuntimeException::new);
        userScoreToUpdate.increaseScore();
        logger.info("User Scores wurden erfolgreich geupdated.");
    }

    public Game getGame() {
        return gameInstance;
    }

    public void setGame(Game game) {
        gameInstance = game;
    }
}