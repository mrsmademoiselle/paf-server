package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

/**
 * Das UserScore Objekt stellt zur Laufzeit einen User mit seinen Zügen dar.
 */
@ToString
@Getter
@Setter
@Embeddable
public class UserScore {
    @OneToOne
    private User user;
    // moves = Punkte
    private int moves;

    @Deprecated
    public UserScore() {
    }

    public UserScore(User user, int moves) {
        this.user = user;
        this.moves = moves;
    }
}