package com.example.uno_test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Game implements Serializable {
    private static final String TAG = "demo";
    public String id;
    public String ownerId;
    public String ownerName;
    public String title;

    public Date createdAt;

    private HashMap<String, Date> presence = new HashMap<>();
    private Card currentTopCard;

    public Game() {
    }

    public Game(String id, String ownerName, String ownerId, String title) {
        this.id = id;
        this.ownerName = ownerName;
        this.ownerId = ownerId;
        this.title = title;
        this.createdAt = new Date();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}



