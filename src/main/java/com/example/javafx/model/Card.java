package com.example.javafx.model;

import javafx.scene.shape.Rectangle;

public class Card extends Rectangle {
    
    private String cardSource;
    private boolean flipped = false;
    private String cardId;

    public boolean getFlipped() {
        return this.flipped;
    }

    public void setFlipped(boolean val) {
        this.flipped = val;
    }


    public String getCardSource() {
        return this.cardSource;
    }

    public void setCardSource(String src) {
        this.cardSource = src;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
