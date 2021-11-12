package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class CardSet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Card> cards;

    public CardSet() {
    }
}