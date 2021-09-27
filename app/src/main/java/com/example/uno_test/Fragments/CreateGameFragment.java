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

import com.example.uno_test.data.Card;
import com.example.uno_test.data.Deck;
import com.example.uno_test.data.Game;
import com.example.uno_test.data.Player;
import com.example.uno_test.databinding.FragmentCreateGameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CreateGameFragment extends Fragment {
    private static final String TAG = "demo";

    private FragmentCreateGameBinding binding;
    Deck deckModel;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Player currentPlayer;
    String creatorName;
    String ownerId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentPlayer = new Player();
        deckModel = new Deck();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUser();

        binding.btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = binding.etTitle.getText().toString();
                Game newGame = new Game(ownerId,creatorName,ownerId,title);
                createGame(newGame);
            }
        });
    }
    private void getUser() {
        DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        creatorName = document.get("name").toString();
                        ownerId = document.get("id").toString();
                        currentPlayer.setPlayerName(creatorName);
                        currentPlayer.setPlayerId(ownerId);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
    private void initDeck(){
        deckModel.initialize();
        //deckModel.shuffle();
    }
    private void addDeckWithArrayList() {
        initDeck();
        ArrayList<Card> cards = deckModel.getCardsList();
        WriteBatch batch = db.batch();
        for (int i = 0; i < cards.size(); i++) {
            DocumentReference card = db.collection("games").document(mAuth.getCurrentUser().getUid()).collection("deck").document(String.valueOf(i));
            batch.set(card, cards.get(i));
        }
        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "isSuccessful: ");
                }
            }
        });
    }
    private void addPlayer1(){
        Map<String,String> name = new HashMap<>();
        name.put("name",currentPlayer.getPlayerName());
         db.collection("games")
                 .document(mAuth.getCurrentUser().getUid())
                 .collection("players")
                 .document(currentPlayer.getPlayerId())
                 .set(name);
    }

    private void createGame(Game game){
        db.collection("games")
                .document(mAuth.getCurrentUser().getUid())
                .set(game)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        addDeckWithArrayList();
                        addPlayer1();
                        NavHostFragment.findNavController(CreateGameFragment.this)
                                .popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document", e);
                        Snackbar.make(getView(), "Error creating the game.", Snackbar.LENGTH_LONG).show();
                    }
                });
    }

}