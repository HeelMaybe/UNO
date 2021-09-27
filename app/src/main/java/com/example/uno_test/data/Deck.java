package com.example.uno_test.data;

import android.util.Log;

import com.example.uno_test.data.Card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Deck {
    private static final String TAG = "demo";

    private Card[] cards;
    private ArrayList<Card> cardsList;
    private int cardsInDeck;
    public Deck() {
        cards = new Card[108];
    }

    public void initialize() {
        Card.Color[] colors = Card.Color.values();
        cardsInDeck = 0;
        cardsList
                = new ArrayList<>();

        for (int i = 0; i < colors.length - 1; i++) {
            Card.Color color = colors[i];
            cards[cardsInDeck++] = new Card(color, Card.Value.getValue(0));

            for (int j = 1; j < 10; j++) {
                cards[cardsInDeck++] = new Card(color, Card.Value.getValue(j));
                cards[cardsInDeck++] = new Card(color, Card.Value.getValue(j));
            }
            Card.Value[] values = new Card.Value[]{Card.Value.DrawTwo, Card.Value.Skip, Card.Value.Reverse};
            for (Card.Value value : values) {

                cards[cardsInDeck++] = new Card(color, value);
                cards[cardsInDeck++] = new Card(color, value);
                //Log.d(TAG, "reset: cards in the deck: "+ String.valueOf(cardsInDeck));
            }

        }
        Card.Value[] values = new Card.Value[]{Card.Value.Wild, Card.Value.WildFour};
        for (Card.Value value : values) {
            for (int i = 0; i < 4; i++) {
                //Log.d(TAG, "reset: ");
                cards[cardsInDeck++] = new Card(Card.Color.Wild, value);
                //Log.d(TAG, "Card.Value.Wild, Card.Value.WildFour reset: cards in the deck: "+ String.valueOf(cardsInDeck));
            }
        }

        for (Card card :cards) {
            cardsList.add(card);
        }
    }
    public boolean isEmpty() {
        return cardsInDeck == 0;
    }

    public Card[] getCards() {
        return cards;
    }

    public ArrayList<Card> getCardsList() {
        return cardsList;
    }
    public void shuffle() {
        cardsList.clear();
        int n = cards.length;
        Random random = new Random();
        for (int i = 0; i < cards.length; i++) {
            int randomValue = i + random.nextInt(n - i);
            Card randomCard = cards[randomValue];
            cards[randomValue] = cards[i];
            cards[i] = randomCard;
            cardsList.add(cards[i]);
        }

    }

    public Card getCurrentTopCard(){
        return cardsList.get(0);
    }

}

