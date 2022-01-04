package com.example.javafx.model;

import java.util.Arrays;
import java.util.Map;


public class GameDto {
    /*TODO: Rework komplett und verschmelzen mit Game aus Websocket
     */

    public GameDto(String lobbyCode, UserDto currentTurn, BoardDto board, UserScoreDto userScores) {
    }


    /**
     * Gegenpruefen ob das Objekt ein valides GameDTO ist
     * @param gameDto Das zu pruefende DTO
     * @return True wenn Keus vorhanden, false wenn nicht
     */
    public static boolean isValidMatchDto(Map<String, String> gameDto) {
        return gameDto.containsKey("lobbycode") && gameDto.containsKey("currentTurn")
                && gameDto.containsKey("board") && gameDto.containsKey("userScores");
    }
}