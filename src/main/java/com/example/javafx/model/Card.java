package com.example.javafx.model;

import javafx.scene.shape.Rectangle;

public class Card extends Rectangle {

    private String pairId;
    private int[][] cardPosition;
    private String cardSource;
    private boolean flipped = false;
    private String cardId;

    public void setFlipped(boolean val){this.flipped=val;}
    public boolean getFlipped(){return this.flipped;}
    // cardId = PairID
    public void setPairId(String id) {this.pairId = id;}
    public void setPosition(int[][] position) {this.cardPosition = position;}
    public String getPairId() {return pairId;}
    public int[][] getCardPosition() {return cardPosition;}
    public void setCardSource(String src){this.cardSource = src;}
    public String getCardSource(){
        return this.cardSource;
    }
    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }
}
