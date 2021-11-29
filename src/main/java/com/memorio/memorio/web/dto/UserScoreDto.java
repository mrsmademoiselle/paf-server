package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserScoreDto {
    private UserAuthDto user;
    private int moves;

    public UserScoreDto(UserAuthDto user, int moves) {
        this.user = user;
        this.moves = moves;
    }
}
