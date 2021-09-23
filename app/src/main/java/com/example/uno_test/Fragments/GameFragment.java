package com.example.uno_test.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uno_test.R;
import com.example.uno_test.data.Card;
import com.example.uno_test.data.Deck;
import com.example.uno_test.data.Game;
import com.example.uno_test.databinding.FragmentGameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GameFragment extends Fragment {
    FragmentGameBinding binding;
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
                addDeck();
            }
        });


    }
    private void initDeck(){
        deckModel.initialize();
        deckModel.shuffle();
        deckModel.shuffle();
    }

    private void addDeck() {
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
        deck.put("deck",cards);
        db.collection("deck")
                .document(mAuth.getUid())
                .set(deck)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "addDeck Successful: ");
                            updateDeckSize(cards);
                        } else {
                            Log.d(TAG, "addDeck notSuccessful: " + task.getException().getMessage());
                            task.getException().printStackTrace();
                        }
                    }
                });



    }
    private void updateDeckSize(Map<String,Map> cards){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("deck")
                .document(mAuth.getUid())
                .update("deckSize",cards.size())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "addDecksize Successful: ");
                        } else {
                            Log.d(TAG, "addDecksize notSuccessful: " + task.getException().getMessage());
                            task.getException().printStackTrace();
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