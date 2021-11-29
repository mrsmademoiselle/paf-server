package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDto{

    private String username;
    private String password;

    public UserAuthDto(String username, String password) {
        this.username = username;
	this.password = password;
    }
}
