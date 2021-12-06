package com.memorio.memorio.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
@Getter
@Setter
public class CardSetDto {

    private long id;
    private String name;
    private List<CardDto> cards;

    public CardSetDto(long id, String name, List<CardDto> cards) {
        this.id = id;
        this.name = name;
        this.cards = cards;
    }
}