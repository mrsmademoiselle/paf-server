package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class UserDataResponse {
    private String username;
    private byte[] profileImage;

    public UserDataResponse(String username, byte[] profileImage){
	this.username = username;
	this.profileImage = profileImage;
    }
}