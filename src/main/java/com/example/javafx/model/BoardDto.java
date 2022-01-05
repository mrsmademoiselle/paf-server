package com.example.javafx.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoardDto {

    private List<Card> cardSet;

    public List<Card> getCardSet() {
        return cardSet;
    }

    public void setCardSet(List<Card> cardSet) {
        this.cardSet = cardSet;
    }

    /*
    public BoardDto() {
        this.cardSet = createCardSet();
    }

    private List<Card> createCardSet() {
        List<Card> cardset = new ArrayList<>();
        for (int i = 2; i <= 17; i++) {
            int pairId = i / 2;
            cardset.add(new Card(pairId));
        }
        Collections.shuffle(cardset);

        return cardset;
    }
     */
}
