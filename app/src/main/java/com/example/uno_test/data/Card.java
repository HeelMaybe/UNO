package com.example.uno_test.data;

import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Card implements Serializable {
    final private String TAG = "demo";

    String color, type;

    public Card(String firebaseCard) {
        String[] items = firebaseCard.split("-");

        if (items.length == 2) {
            this.color = items[0];
            this.type = items[1];
//        } else if (items.length == 1){
//            this.type = items[0]; //DRAW4
//            this.color = "BLACK";
//        }
        }
    }

    public boolean canPlayCard(String topCard) {
        String[] items = topCard.split("-");

        if (this.color.equals(items[0])) {
            Log.d(TAG, "canPlayCard same color: true ");
            return true;
        } else if (this.type.equals(items[1])) {
            Log.d(TAG, "canPlayCard same type: true ");
            return true;
        } else if (this.type.equals("DRAW4")) {
            Log.d(TAG, "canPlayCard DRAW4: true ");
            return true;
        } else if (items[0].equals("BLACK")) {
            Log.d(TAG, "canPlayCard played DRAW4: true ");
            return true;
        }
        Log.d(TAG, "canPlayCard: false ");
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
