package com.example.uno_test.data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Card implements Serializable {

    String color, type;

    public Card(String firebaseCard) {
        String[] items = firebaseCard.split("-");
        for (String item : items) {
            item.replaceAll("[(){}]","");
        }
        if(items.length == 2){
            color = items[0];
            type = items[1];
        } else if (items.length == 1){
            type = items[0]; //DRAW4
            color = "BLACK";
        }
    }

    public boolean canPlayCard(Card topCard){
        if(this.color.equals(topCard.color)){
            return true;
        } else if(this.type.equals(topCard.type)){
            return true;
        } else if(this.type.equals("DRAW4")){
            return true;
        } else if(topCard.type.equals("DRAW4")){
            return true;
        }
        return false;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
