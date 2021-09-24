package com.example.uno_test.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uno_test.data.Card;
import com.example.uno_test.data.Deck;
import com.example.uno_test.data.Game;
import com.example.uno_test.data.Player;
import com.example.uno_test.databinding.FragmentGameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameFragment extends Fragment {
    public static String GAME_KEY = "GAME_KEY";
    public static String GAME_TITLE = "GAME_TITLE";
    FragmentGameBinding binding;
    Game uno;
    private FirebaseAuth mAuth;
    final private String TAG = "demo";
    Deck deckModel;
    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        deckModel = new Deck();
        uno = new Game();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDeckWithMaps();
            }
        });


    }
    private void initDeck(){
        deckModel.initialize();
        //deckModel.shuffle();
    }

    private void addDeckWithMaps() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        initDeck();
        Map<String, Map> deck = new HashMap<>();
        Map<String, Map> cards = new HashMap<>();
        for (int i = 0; i < deckModel.getCards().length; i++) {
            Map<String, String > card = new HashMap<>();
            String color = String.valueOf(deckModel.getCardsList().get(i).getColor());
            String value = String.valueOf(deckModel.getCardsList().get(i).getValue());
            card.put("color",color);
            card.put("value",value);
            cards.put(String.valueOf(i),card);
        }
        deck.put("tableDeck",cards);

        db.collection("games").document("game1")
                .set(deck)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "addDeck Successful: ");
                           //getCurrentTopCard();
                        } else {
                            Log.d(TAG, "addDeck notSuccessful: " + task.getException().getMessage());
                            task.getException().printStackTrace();
                        }
                    }
                });
    }
    private void addDeckWithArray() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        initDeck();
        Map<String, Map> deck = new HashMap<>();

        db.collection("games").document("game1")
                .set(deckModel.getCardsList())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "addDeck Successful: ");
                            //getCurrentTopCard();
                        } else {
                            Log.d(TAG, "addDeck notSuccessful: " + task.getException().getMessage());
                            task.getException().printStackTrace();
                        }
                    }
                });
    }

    private void getCurrentTopCard(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("deck")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "getCurrentTopCard Successful: " + document.getData());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "getCurrentTopCard notSuccessful: " + task.getException().getMessage());
                    task.getException().printStackTrace();
                }
            }
        });
    }

    private void setPlayer2Name(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String name = task.getResult().getString("name");
                    Player one = new Player(name);
                    uno.setPlayerOne(one);
                    Map<String ,String> playerMap = new HashMap<>();
                    playerMap.put("setPlayer2Name",one.getPlayerName());
                    db.collection("games")
                            .document("game1")
                            .set(playerMap, SetOptions.merge())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "setPlayer2Name Successful: ");
                                    } else {
                                        Log.d(TAG, "setPlayer2Name notSuccessful: " + task.getException().getMessage());
                                        task.getException().printStackTrace();
                                    }
                                }
                            });
                }
            }
        });
    }

    //    private void registerPresence() {
//        chatRoom.presence.put(loggedUserId, new Date());
//
//        db.collection("rooms")
//                .document(roomId)
//                .update("presence", chatRoom.presence)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.d("CRDebug", "Presence updated successfully");
//                    }
//                });
//    }
}