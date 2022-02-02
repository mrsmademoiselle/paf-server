package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Random;

/**
 * Das UserScore Objekt stellt zur Laufzeit einen User mit seinen Zügen dar.
 */
@ToString
@Getter
@Setter
@Entity
public class UserScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;
    @Column
    private int moves;

    @Deprecated
    public UserScore() {
    }

    /**
     * Verknuepft User mit userscore
     * @param user
     */
    public UserScore(User user) {
        this.user = user;
        this.moves = 0;
    }

    public void increaseScoreByRandomAmount() {
        // random number 1-10
        this.moves += new Random().nextInt(10 - 1) + 1;
    }

    /**
     * Erhoeht Score um Param
     * @param i Scoreerhoehung
     */
    public void increaseScoreBy(int i) {
        this.moves += i;
    }

    /**
     * Erhoeht Score um 1
     */
    public void increaseScore() {
        this.moves++;
    }
}