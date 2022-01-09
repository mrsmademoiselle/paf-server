package com.memorio.memorio.web.dto;

import com.memorio.memorio.entities.User;
import com.memorio.memorio.entities.UserScore;
import com.memorio.memorio.exceptions.MemorioRuntimeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Das EndscoreDto Objekt
 */
@ToString
@Getter
@Setter
public class EndscoreDto {
    private User winner;

    private List<UserScore> scoreListe;

    @Deprecated
    public EndscoreDto() {
    }

    public EndscoreDto(List<UserScore> scoreListe) {
        //Zum identifizieren des Gewinners werden die Punkte abgeglichen
        this.scoreListe = scoreListe;

        if (scoreListe.size() != 2)
            throw new MemorioRuntimeException("EndscoreDto: Es müssen genau 2 UserScores existieren!");

        this.winner = setWinner(scoreListe);
    }

    private User setWinner(List<UserScore> scoreListe) {
        UserScore user1 = scoreListe.get(0);
        UserScore user2 = scoreListe.get(1);

        if (user1.getMoves() < user2.getMoves()) {
            return user2.getUser();
        } else { // auch wenn unentschieden ist, gewinnt stumpf user1 :-)
            return user1.getUser();
        }
    }
}