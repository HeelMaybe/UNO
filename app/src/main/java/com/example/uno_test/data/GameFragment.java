package com.example.uno_test.data;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uno_test.Adapters.DeckAdapter;
import com.example.uno_test.data.Card;
import com.example.uno_test.data.Deck;
import com.example.uno_test.data.Game;
import com.example.uno_test.data.Player;
import com.example.uno_test.databinding.FragmentGameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameFragment extends Fragment {
    public static String GAME_KEY = "GAME_KEY";
    public static String GAME_TITLE = "GAME_TITLE";
    FragmentGameBinding binding;
    private String gameId;
    private String gameTitle;
    private String loggedPlayerId;

    private RecyclerView rvCards;
    private DeckAdapter rvaCards;
    private LinearLayoutManager lmCards;
    private ArrayList<Card> cards = new ArrayList<>();

    Game uno;
    private FirebaseAuth mAuth;
    final private String TAG = "demo";
    Deck deckModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        uno = new Game();
        db = FirebaseFirestore.getInstance();
        deckModel = new Deck();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        gameId = bundle.getString(GAME_KEY);
        gameTitle = bundle.getString(GAME_TITLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        loggedPlayerId = mAuth.getCurrentUser().getUid();

        binding = FragmentGameBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpCardRecyclerView();
        getCards();
        binding.buttonCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }


    private void setUpCardRecyclerView(){
        rvCards = binding.rvCards;
        rvCards.setHasFixedSize(true);

        lmCards = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        lmCards.setReverseLayout(true);
        lmCards.setStackFromEnd(false);
        rvCards.setLayoutManager(lmCards);

        rvaCards = new DeckAdapter(cards, new DeckAdapter.ICardRow() {
            @Override
            public void onCardSelected(Card card) {
                Log.d(TAG, "onCardSelected: card:" + card.getColor().toString() +"," + card.getValue().toString());
            }
        });
        rvCards.setAdapter(rvaCards);

    }

    private void getCards(){
        cards.clear();
        for (int i = 0; i<7;i++){
            db.collection("games")
                    .document(gameId)
                    .collection("deck")
                    .document(String.valueOf(i))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot doc) {
                    Card card = new Card(Card.Color.valueOf(doc.get("color").toString()), Card.Value.valueOf(doc.get("value").toString()));
                    Log.d(TAG, "getCards Card: "+doc.get("color").toString()+","+ doc.get("value").toString() );
                    cards.add(card);
                }
            });
        }
        rvaCards.notifyDataSetChanged();

    }
    private void drawCard(){

    }










    private void getGame(){
        DocumentReference docRef = db.collection("games").document(gameId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        uno = document.toObject(Game.class);
                    }else{
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    private void getCurrentTopCard(){

        db.collection("games")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

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
        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String name = task.getResult().getString("name");
                    Player one = new Player(name);
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