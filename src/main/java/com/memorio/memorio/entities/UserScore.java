package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

/**
 * Das UserScore Objekt stellt zur Laufzeit einen User mit seinen Zügen dar.
 */
@Getter
@Setter
@Embeddable
public class UserScore {
    @OneToOne
    private User user;
    private int moves;

    @Deprecated
    public UserScore() {
    }

    public UserScore(User user, int moves) {
        this.user = user;
        this.moves = moves;
    }
}