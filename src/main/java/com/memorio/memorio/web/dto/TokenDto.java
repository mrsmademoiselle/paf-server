package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TokenDto implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;

    public TokenDto(String jwttoken) {
        this.jwttoken = jwttoken;
    }
}