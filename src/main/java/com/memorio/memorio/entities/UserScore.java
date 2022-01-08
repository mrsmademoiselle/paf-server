package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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

    public UserScore(User user) {
        this.user = user;
        this.moves = 0;
    }

    public void increaseScore() {
        this.moves++;
    }
}