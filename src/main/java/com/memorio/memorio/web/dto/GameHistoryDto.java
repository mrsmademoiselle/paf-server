package com.memorio.memorio.web.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class GameHistoryDto {
    private int totalGames;
    private int wins;
    private int losses;
    private int averageMoves;

    @Deprecated
    public GameHistoryDto() {
    }

    public GameHistoryDto(int totalGames, int wins, int losses, int averageMoves) {
        this.totalGames = totalGames;
        this.wins = wins;
        this.losses = losses;
        this.averageMoves = averageMoves;
    }
}