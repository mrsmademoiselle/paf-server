package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MatchDto {

    private long lobbyCode;
    private List<UserScoreDto> userScores;

    public MatchDto(long lobbycode, List<UserScoreDto> userscores) {
        this.lobbyCode = lobbycode;
        this.userScores = userscores;
    }
}