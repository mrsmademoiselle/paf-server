package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

/**
 * Das UserProfil stellt das Profil des Nutzers dar,
 * in dem seine Statistiken angezeigt werden können.
 */
@ToString
@Getter
@Setter
@Entity
public class UserProfil {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    // Die könnte man auch über gameHistory filtern, anstatt sie alle als statische Werte zu speichern.
    // Oder man macht dafür ein MatchHistory Objekt, das die List<Game> sowie die einzelnen Werte hat und
    // bei der Objekterstellung setzt
    private int totalWins;
    private int totalLosses;
    private int totalMatches;
    // Alle abgeschlossenen Matches des UserProfils
    @OneToMany
    // Lazy Loaded field wird von toString ausgeschlossen, damit keine PerformanceProbleme auftreten
    @ToString.Exclude
    private List<Game> gameHistory;

    @Deprecated
    public UserProfil() {
    }

    public UserProfil(int winsSum, int lossesSum, int matchSum, List<Game> gameHistory) {
        this.totalWins = winsSum;
        this.totalLosses = lossesSum;
        this.totalMatches = matchSum;
        this.gameHistory = gameHistory;
    }
}