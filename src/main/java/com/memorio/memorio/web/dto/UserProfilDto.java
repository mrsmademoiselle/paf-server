package com.memorio.memorio.web.dto;

import com.memorio.memorio.entities.UserProfil;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserProfilDto {
    private long id;
    private UserProfil userProfil;
    private int wins;
    private int looses;
    private int matchSum;
    private List<MatchDto> history;

    public UserProfilDto(long id, int wins, int looses, List<MatchDto> history, int matchSum) {
        this.wins = wins;
        this.looses = looses;
        this.history = history;
        this.matchSum = matchSum;
    }
}