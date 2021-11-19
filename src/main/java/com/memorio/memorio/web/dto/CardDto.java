package com.memorio.memorio.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {

    private long id;
    private boolean isFlipped;
    private String imagePath;


    public CardDto(long id, boolean isFlipped, String imagePath) {
        this.id = id;
        this.imagePath = imagePath;
        this.isFlipped = isFlipped;
    }
}