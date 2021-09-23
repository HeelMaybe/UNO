package com.example.uno_test.data;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class Game {
    private static final String TAG = "demo";
    private Card[] cardsInMiddle = new Card[108];
    private Player playerOne;
    private Player playerTwo;
    private Player[]players = new Player[2];
    public HashMap<String, Date> presence = new HashMap<>();


    public Game() {
    }

    public Game(Card[] cardsInMiddle, Player playerOne, Player playerTwo, Player[] players) {
        this.cardsInMiddle = cardsInMiddle;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        players[0] = playerOne;
        players[1] = playerTwo;
    }

    @Override
    public String toString() {
        return "Board{" +
                "players=" + Arrays.toString(players) +
                '}';
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player player1, Player player2) {
        players[0] = player1;
        players[1] = player2;
    }

    public Card[] getCardsInMiddle() {
        return cardsInMiddle;
    }

    public void setCardsInMiddle(Card[] cardsInMiddle) {
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
