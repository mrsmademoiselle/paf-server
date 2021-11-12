package com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Board {
    private int boardSize;
    private CardSet cardSet;

    public Board(){
    }
}