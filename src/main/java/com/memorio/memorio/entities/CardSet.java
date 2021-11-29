package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Ein CardSet ist eine Ansammlung von Cards, die benannt werden kann.
 * Dadurch können später eigene Kartensets angelegt und im Profil verändert werden.
 */
@Getter
@Setter
@Entity
public class CardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Card> cards;

    @Deprecated
    public CardSet() {
    }

    public CardSet(String name, List<Card> cards) {
        this.name = name;
        this.cards = cards;
    }
}