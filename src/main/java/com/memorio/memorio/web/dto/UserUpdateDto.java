package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@Setter

public class UserUpdateDto {

    @Pattern(regexp = "[A-Za-z0-9]+$")
    private String username;

    private String password;
    private byte[] img;

    @Deprecated
    public UserUpdateDto(){}

    public UserUpdateDto(String username, String password, byte[] img){
	this.username = username;
	this.password = password;
	this.img = img;
    }
     
}
