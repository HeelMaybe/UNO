package com.example.uno_test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Game implements Serializable {
    private static final String TAG = "demo";
    public String id;
    public String ownerId;
    public String ownerName;
    public String title;

    public Date createdAt;
    private ArrayList<Card> cardsInMiddle = new ArrayList<>();
    private Deck tableDeck;
    private Player playerOne;
    private Player playerTwo;
    private HashMap<String, Date> presence = new HashMap<>();
    private Card currentTopCard;

    public Game() {
    }

    public Game(String id, String ownerName,String ownerId, String title,Player player1) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.title = title;
        this.createdAt = new Date();
        this.playerOne = player1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Game{" +
                "players= " + playerOne + " "
                + playerTwo + '}';
    }

    public ArrayList<Card> getCardsInMiddle() {
        return cardsInMiddle;
    }

    public void setCardsInMiddle(ArrayList<Card> cardsInMiddle) {
        this.cardsInMiddle = cardsInMiddle;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

}
