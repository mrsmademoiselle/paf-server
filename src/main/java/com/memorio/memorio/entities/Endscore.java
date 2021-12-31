package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Das Endscore Objekt
 */
public class Endscore{
    private User winner;

    private List<UserScore> scoreListe;

    @Deprecated
    public Endscore() {
    }

    public Endscore(List<UserScore> scoreListe) {
        /*Zum identifiezieren des Gewinners werden die Punkte abgeglichen
        * */
        this.scoreListe = scoreListe;

        if(scoreListe.get(0).getMoves() < scoreListe.get(1).getMoves()) {
            this.winner = scoreListe.get(1).getUser();
        }else if(scoreListe.get(0).getMoves() == scoreListe.get(1).getMoves()){
            this.winner = null;
        }
        else{
            this.winner = scoreListe.get(0).getUser();
        }
    }
}