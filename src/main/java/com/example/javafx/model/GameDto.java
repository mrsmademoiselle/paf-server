package com.example.javafx.model;

import java.util.Arrays;
import java.util.Map;


public class GameDto {
    /*TODO: Rework komplett und verschmelzen mit Game aus Websocket
     */
    private String lobbyCode;
    private UserDto currentTurn;
    private BoardDto board;
    private UserScoreDto userScores;

    /**
     * Gegenpruefen ob das Objekt ein valides GameDTO ist
     * @param gameDto Das zu pruefende DTO
     * @return True wenn Keus vorhanden, false wenn nicht
     */
    public static boolean isValidMatchDto(Map<String, String> gameDto) {
        return gameDto.containsKey("lobbycode") && gameDto.containsKey("currentTurn")
                && gameDto.containsKey("board") && gameDto.containsKey("userScores");
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    public void setLobbyCode(String lobbyCode) {
        this.lobbyCode = lobbyCode;
    }

    public UserDto getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(UserDto currentTurn) {
        this.currentTurn = currentTurn;
    }

    public BoardDto getBoard() {
        return board;
    }

    public void setBoard(BoardDto board) {
        this.board = board;
    }

    public UserScoreDto getUserScores() {
        return userScores;
    }

    public void setUserScores(UserScoreDto userScores) {
        this.userScores = userScores;
    }
}