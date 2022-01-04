package com.example.javafx.model;

import javafx.scene.shape.Rectangle;

public class Card extends Rectangle {

    private String cardId;
    private int[][] cardPosition;
    private String cardSource;
    private boolean flipped = false;

    public void setFlipped(boolean val){this.flipped=val;}
    public boolean getFlipped(){return this.flipped;}
    // cardId = PairID
    public void setCardId(String id) {this.cardId = id;}
    public void setPosition(int[][] position) {this.cardPosition = position;}
    public String getCardId() {return cardId;}
    public int[][] getCardPosition() {return cardPosition;}
    public void setCardSource(String src){this.cardSource = src;}
    public String getCardSource(){
        return this.cardSource;
    }
}
