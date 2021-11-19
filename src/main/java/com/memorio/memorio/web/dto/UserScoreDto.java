package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserScoreDto {
    private UserDto user;
    private int moves;

    public UserScoreDto(UserDto user, int moves) {
        this.user = user;
        this.moves = moves;
    }
}