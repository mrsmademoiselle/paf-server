package com.example.javafx.model;

import javafx.scene.shape.Rectangle;

public class Card extends Rectangle {

    private byte[] cardImage;
    private String cardId;
    private int[][] cardPosition;

    public void setImage(byte[] image) {this.cardImage = image;}
    public void setCardId(String id) {this.cardId = id;}
    public void setPosition(int[][] position) {this.cardPosition = position;}
    public byte[] getCardImage() {return cardImage;}
    public String getCardId() {return cardId;}
    public int[][] getCardPosition() {return cardPosition;}
}
