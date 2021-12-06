package com.memorio.memorio.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
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