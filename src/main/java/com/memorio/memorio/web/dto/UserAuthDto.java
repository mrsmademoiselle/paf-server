package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@Setter
public class UserAuthDto {

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9]+$")
    private String username;
    @NotBlank
    private String password;

    public UserAuthDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}