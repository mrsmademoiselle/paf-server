package com.memorio.memorio.web.dto;

import com.memorio.memorio.entities.User;
import com.memorio.memorio.entities.UserScore;
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