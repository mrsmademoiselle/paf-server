package com.memorio.memorio.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Das UserProfil stellt das Profil des Nutzers dar,
 * in dem seine Statistiken angezeigt werden können.
 */
@EqualsAndHashCode
@ToString
@Getter
@Setter
@Entity
/* statt wins/losses ein UserProfile Objekt, das eine List<Match> beinhält,
 * gesamtwins, gesamtlosses, averagewins (als Methode im Material selbst anhand der Matches
 * berechnen) */
public class UserProfil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int totalWins;
    private int totalLosses;
    private int totalMatches;
    // Alle abgeschlossenen Matches des UserProfils
    @OneToMany
    private List<Match> history;

    @Deprecated
    public UserProfil() {
    }

    public UserProfil(int winsSum, int lossesSum, int matchSum, List<Match> history) {
        this.totalWins = winsSum;
        this.totalLosses = lossesSum;
        this.totalMatches = matchSum;
        this.history = history;
    }
}