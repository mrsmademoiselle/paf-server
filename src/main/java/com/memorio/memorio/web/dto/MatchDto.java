package com.memorio.memorio.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
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