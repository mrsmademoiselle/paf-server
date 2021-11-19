package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private long id;
    private String username;
    private UserProfilDto wins;

    public UserDto(long id, String username, UserProfilDto wins) {
        this.id = id;
        this.username = username;
        this.wins = wins;
    }
}