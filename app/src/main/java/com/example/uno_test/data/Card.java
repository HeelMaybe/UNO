package com.example.uno_test.data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Card implements Serializable {

    enum Color {
        Red,Blue,Green, Yellow, Wild;
        private static final Color[] colors = Color.values();
        public static Color getColor(int i){
            return Color.colors[i];
        }

    }
    enum Value {
        zero,one,two,three,four,five,six,seven,eight,nine,DrawTwo,Skip,Reverse,Wild,WildFour;
        private static final Value[] values = Value.values();
        public static Value getValue(int i){
            return Value.values[i];
        }

    }

    private final Color color;

    private final Value value;

    public Card(final Color color,final Value value) {
        this.color = color;
        this.value = value;
    }


    public Color getColor() {
        return color;
    }

    public Value getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, value);
    }

    @Override
    public String toString() {
        return "Card{" +
                "color=" + color +
                ", value=" + value +
                '}';
    }
}
