package com.example.uno_test.data;

import java.util.Arrays;

public class Player {
    private String playerName;
    private Card[] playerCards;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Card[] getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(Card[] playerCards) {
        this.playerCards = playerCards;
    }

    public Player(String playerName) {
        this.playerName = playerName;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerName='" + playerName + '\'' +
                ", playerCards=" + Arrays.toString(playerCards) +
                '}';
    }
}
