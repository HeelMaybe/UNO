package com.example.uno_test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class Game implements Serializable {
    private static final String TAG = "demo";
    public String gameId;
    public String ownerId;
    public String ownerName;
    public String title;
    public ArrayList<String> floorCardsDeck = new ArrayList<>();
    public ArrayList<String> player1CardsDeck = new ArrayList<>();
    public ArrayList<String> player2CardsDeck = new ArrayList<>();
    public String currentCard;
    public String player1Id, player1Name;
    public String player2Id, player2Name;
    public String status = "CREATED"; //CREATED, STARTED, ENDED
    public int whosTurn = 1;


    public Date createdAt;

    private HashMap<String, Date> presence = new HashMap<>();

    public Game() {
    }

    public Game(String id, String ownerName, String ownerId, String title) {
        this.gameId = id;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.title = title;
        this.createdAt = new Date();

        this.status = "CREATED";

        this.player1Id = ownerId;
        this.player1Name = ownerName;
        this.whosTurn = 1; //Player 1
    }

    public void generateStartDeck(){

        for (int i = 0; i < 10 ; i++) {
            this.floorCardsDeck.add("RED-" + i);
            this.floorCardsDeck.add("RED-" + i);

            this.floorCardsDeck.add("GREEN-" + i);
            this.floorCardsDeck.add("GREEN-" + i);

            this.floorCardsDeck.add("BLUE-" + i);
            this.floorCardsDeck.add("BLUE-" + i);

            this.floorCardsDeck.add("YELLOW-" + i);
            this.floorCardsDeck.add("YELLOW-" + i);
        }
        Collections.shuffle(floorCardsDeck);
        currentCard = floorCardsDeck.remove(0);

        this.floorCardsDeck.add("BLACK-"+"DRAW4");
        this.floorCardsDeck.add("BLACK-"+"DRAW4");
        this.floorCardsDeck.add("BLACK-"+"DRAW4");
        this.floorCardsDeck.add("BLACK-"+"DRAW4");

        this.floorCardsDeck.add("RED-" + "SKIP");
        this.floorCardsDeck.add("RED-" + "SKIP");

        this.floorCardsDeck.add("GREEN-" + "SKIP");
        this.floorCardsDeck.add("GREEN-" +"SKIP");

        this.floorCardsDeck.add("BLUE-" +"SKIP");
        this.floorCardsDeck.add("BLUE-" + "SKIP");

        this.floorCardsDeck.add("YELLOW-" + "SKIP");
        this.floorCardsDeck.add("YELLOW-" + "SKIP");
        Collections.shuffle(floorCardsDeck);

        for (int i = 0; i < 7; i++) {
            player1CardsDeck.add(floorCardsDeck.remove(0));
            player2CardsDeck.add(floorCardsDeck.remove(0));
        }
    }

}



