package com.memorio.memorio.entities;

import java.util.List;

/**
 * Das Endscore Objekt
 */
public class Endscore {
    private User winner;

    private List<UserScore> scoreListe;

    @Deprecated
    public Endscore() {
    }

    public Endscore(List<UserScore> scoreListe) {
        //Zum identifiezieren des Gewinners werden die Punkte abgeglichen
        this.scoreListe = scoreListe;

        UserScore user1 = scoreListe.get(0);
        UserScore user2 = scoreListe.get(1);

        if (user1.getMoves() < user2.getMoves()) {
            this.winner = user2.getUser();
        } else if (user1.getMoves() == user2.getMoves()) {
            this.winner = null;
        } else {
            this.winner = user1.getUser();
        }
    }
}