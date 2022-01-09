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
        this.totalGames = 0;
        this.wins = 0;
        this.losses = 0;
        this.averageMoves = 0;
    }

    public GameHistoryDto(int totalGames, int wins, int averageMoves) {
        this.totalGames = totalGames;
        this.wins = wins;
        this.losses = totalGames - wins;
        this.averageMoves = averageMoves;
    }
}