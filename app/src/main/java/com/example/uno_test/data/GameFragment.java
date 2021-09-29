package com.example.uno_test.data;

import android.graphics.Color;
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
import android.widget.Toast;

import com.example.uno_test.Adapters.DeckAdapter;
import com.example.uno_test.databinding.FragmentGameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class GameFragment extends Fragment {
    public static String GAME_KEY = "GAME_KEY";
    public static String GAME_TITLE = "GAME_TITLE";
    FragmentGameBinding binding;
    private String gameId;
    private String gameTitle;
    private String loggedPlayerId;

    private RecyclerView rvCards;
    private RecyclerView rvCards2;
    private DeckAdapter rvaCards2;

    private DeckAdapter rvaCards;
    private LinearLayoutManager lmCards;
    private ArrayList<Card> player1Cards = new ArrayList<>();
    private ArrayList<Card> player2Cards = new ArrayList<>();

    private String getGameKey;
    private String getGameTitle;

    Game uno;
    private FirebaseAuth mAuth;
    final private String TAG = "demo";
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
        if (getArguments() != null) {
            getGameKey = getArguments().getString(GAME_KEY);
            getGameTitle = getArguments().getString(GAME_TITLE);
        }
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
        getCards();
        setUpPlayer1CardRecyclerView();
        setUpPlayer2CardRecyclerView();

    }

    private void setUpPlayer1CardRecyclerView(){
        rvCards = binding.rvCards;
        rvCards.setHasFixedSize(true);

        lmCards = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        lmCards.setReverseLayout(true);
        lmCards.setStackFromEnd(false);
        rvCards.setLayoutManager(lmCards);
        rvCards.setClickable(false);
        rvaCards = new DeckAdapter(player1Cards, new DeckAdapter.ICardRow() {
            @Override
            public void onCardSelected(Card card) {
                if(card.canPlayCard(uno.currentCard) && uno.whosTurn == 1 && rvCards.isClickable()){
                    db.collection("games")
                            .document(gameId)
                            .update("currentCard",card.getColor()+"-"+card.getType())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                        player1Cards.remove(card);
                                        playCard(card);
                                }
                            });
                }else{
                    Toast.makeText(getContext(), "You cannot play this card", Toast.LENGTH_SHORT).show();

                }
            }
        });
        rvCards.setAdapter(rvaCards);
    }
    private void setUpPlayer2CardRecyclerView(){
        rvCards2 = binding.rvCards2;
        rvCards2.setHasFixedSize(true);

        lmCards = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        lmCards.setReverseLayout(true);
        lmCards.setStackFromEnd(false);
        rvCards2.setLayoutManager(lmCards);

        rvaCards2 = new DeckAdapter(player2Cards, new DeckAdapter.ICardRow() {
            @Override
            public void onCardSelected(Card card) {
                if(card.canPlayCard(uno.currentCard) && uno.whosTurn == 2 && rvCards2.isClickable()){
                    db.collection("games")
                            .document(gameId)
                            .update("currentCard",card.getColor()+"-"+card.getType())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    player2Cards.remove(card);
                                    playCard(card);
                                }
                            });
                }else{
                    Toast.makeText(getContext(), "You cannot play this card", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rvCards2.setAdapter(rvaCards2);
    }


    private void upDateCurrentCard(String currentCard){
       Card topCard = new Card(currentCard);
       binding.tvTopCard.setText(topCard.getType());
        if(topCard.getType().equals("SKIP")){
            binding.tvTopCard.setText(topCard.getType());
        }else if(topCard.getType().equals("DRAW4")){
            binding.tvTopCard.setText("+4");
        }
        if(topCard.getColor().equals("RED")){
            binding.tvTopCard.setBackgroundColor(Color.RED);
        }else if(topCard.getColor().equals("GREEN")){
            binding.tvTopCard.setBackgroundColor(Color.GREEN);
        }else if(topCard.getColor().equals("YELLOW")){
            binding.tvTopCard.setBackgroundColor(Color.YELLOW);
        }else if(topCard.getColor().equals("BLUE")){
            binding.tvTopCard.setBackgroundColor(Color.BLUE);
        }else if(topCard.getType().equals("DRAW4")){
            binding.tvTopCard.setBackgroundColor(Color.BLACK);
        }
    }

    private void getCards(){
            db.collection("games")
                    .document(gameId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    uno = value.toObject(Game.class);

                    if((uno.whosTurn == 1&& value.get("status").equals("CREATED"))){
                        binding.tvPlayerTurn.setText(uno.player1Name +"'s turn");
                    }else if((uno.whosTurn == 1&& value.get("status").equals("PLAYING"))){
                        rvCards2.setClickable(false);
                        binding.tvPlayerTurn.setText(uno.player1Name +"'s turn");
                        rvCards.setClickable(true);
                    }else if (uno.whosTurn == 2){
                        rvCards.setClickable(false);
                        binding.tvPlayerTurn.setText(uno.player2Name +"'s turn");
                        rvCards2.setClickable(true);
                    }
                    isGameFinished();



                    binding.buttonDrawCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(mAuth.getCurrentUser().getUid().equals(uno.player1Id) && uno.whosTurn == 1){
                                //TODO add card to player1 hand
                                DrawCardFromDeck(value,player1Cards,rvaCards);
                            }else if(mAuth.getCurrentUser().getUid().equals(uno.player2Id)&& uno.whosTurn == 2){
                                //TODO add card to player2 hand
                                DrawCardFromDeck(value,player2Cards,rvaCards2);
                            }
                        }
                    });
                        //TODO: fix draw four method
//                    if(value.get("currentCard").equals("BLACK-DRAW4")&&uno.whosTurn == 1){
//                        Draw4CardsFromDeck(value,player2Cards,rvaCards2);
//                    }else if(value.get("currentCard").equals("BLACK-DRAW4")&&uno.whosTurn == 2){
//                        Draw4CardsFromDeck(value,player1Cards,rvaCards);
//                    }

                    upDateCurrentCard(value.get("currentCard").toString());
                    updatePlayer2();

                    //setPlayerOne's hand
                    player1Cards.clear();
                    Log.d(TAG, "onSuccess: player1CardsDeck:"+ value.get("player1CardsDeck").toString() );
                    String arrayOfCards = value.get("player1CardsDeck").toString();
                    String result = arrayOfCards.replaceAll("[\\p{Ps}\\p{Pe}]", "");
                    Log.d(TAG, "onSuccess: player1CardsDeck result:"+ result );
                    String[] items = result.split(", ");
                    for (int i = 0; i<items.length;i++){
                        Card card = new Card(items[i]);
                        player1Cards.add(card);
                    }
                    if(mAuth.getCurrentUser().getUid().equals(uno.player1Id)){
                        rvCards2.setVisibility(View.INVISIBLE);
                    }
                    rvaCards.notifyDataSetChanged();


                    //setPlayerTwo's hand
                    player2Cards.clear();
                    Log.d(TAG, "onSuccess: player2CardsDeck:"+ value.get("player2CardsDeck").toString() );
                    String arrayOfCards2 = value.get("player2CardsDeck").toString();
                    String result2 = arrayOfCards2.replaceAll("[\\p{Ps}\\p{Pe}]", "");
                    Log.d(TAG, "onSuccess: player2CardsDeck result:"+ result2 );
                    String[] items2 = result2.split(", ");
                    for (int i = 0; i<items2.length;i++){
                        Card card = new Card(items2[i]);
                        player2Cards.add(card);
                    }
                    if(mAuth.getCurrentUser().getUid().equals(uno.player2Id)){
                        rvCards.setVisibility(View.INVISIBLE);
                    }
                    rvaCards2.notifyDataSetChanged();
                }
            });
    }

    private void playCard(Card card){
        if(mAuth.getCurrentUser().getUid().equals(uno.player1Id)){
            DocumentReference docRef = db.collection("games").document(gameId);
            if(card.getType().equals("SKIP")){
                docRef.update("player1CardsDeck", FieldValue.arrayRemove(card.getColor()+"-"+card.getType()),
                        "whosTurn",1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                rvaCards.notifyDataSetChanged();
                            }
                        });
            }else if(card.getType().equals("DRAW4")){
                docRef.update("player1CardsDeck", FieldValue.arrayRemove(card.getColor()+"-"+card.getType()),
                        "whosTurn",2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                rvaCards.notifyDataSetChanged();
                            }
                        });
            }
            else{
                docRef.update("player1CardsDeck", FieldValue.arrayRemove(card.getColor()+"-"+card.getType()),
                        "whosTurn",2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                rvaCards.notifyDataSetChanged();
                            }
                        });
            }


        }else if(mAuth.getCurrentUser().getUid().equals(uno.player2Id)){
            DocumentReference docRef = db.collection("games").document(gameId);
            if(card.getType().equals("SKIP")){
                docRef.update("player2CardsDeck", FieldValue.arrayRemove(card.getColor()+"-"+card.getType()),
                        "whosTurn",2)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                rvaCards2.notifyDataSetChanged();
                            }
                        });
            }else if(card.getType().equals("DRAW4")){
                docRef.update("player1CardsDeck", FieldValue.arrayRemove(card.getColor()+"-"+card.getType()),
                        "whosTurn",1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                rvaCards.notifyDataSetChanged();
                            }
                        });
            }
            else{
                docRef.update("player2CardsDeck", FieldValue.arrayRemove(card.getColor()+"-"+card.getType()),
                        "whosTurn",1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                rvaCards2.notifyDataSetChanged();
                            }
                        });
            }


        }


    }

    private void updatePlayer2(){
        if(!mAuth.getCurrentUser().getUid().equals(uno.player1Id)){
            db.collection("users")
                    .document(mAuth.getCurrentUser().getUid())
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    db.collection("games").document(gameId)
                            .update(
                              "player2Id",documentSnapshot.get("id"),
                                    "player2Name",documentSnapshot.get("name"),
                                    "status","PLAYING"
                            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            rvCards.setClickable(true);
                        }
                    });
                }
            });
        }
    }

    private void DrawCardFromDeck(DocumentSnapshot value,ArrayList<Card> playerCards,DeckAdapter adapter){
        //get floor deck
        String arrayOfCards = value.get("floorCardsDeck").toString();
        String result = arrayOfCards.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        Log.d(TAG, "onSuccess: floorCardsDeck result:"+ result );
        String[] items = result.split(", ");
        //get index 0 card string
        //convert to card
        Card card = new Card(items[0]);
        //remove card from floordeck
        DocumentReference docRef = db.collection("games").document(gameId);
        docRef.update("floorCardsDeck", FieldValue.arrayRemove(card.getColor()+"-"+card.getType()));
        //check to see if card can be played
        if(card.canPlayCard(uno.currentCard)){
            //yes-set card to currentTopCard
            docRef.update("currentCard",card.getColor()+"-"+card.getType())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            playCard(card);
                        }
                    });
        }else{
            //no-put in players hand
            if(uno.whosTurn == 1){
                db.collection("games").document(gameId).update("player1CardsDeck", FieldValue.arrayUnion(card.getColor()+"-"+card.getType()));
                playerCards.add(card);
                docRef.update("whosTurn",2);
                adapter.notifyDataSetChanged();
            }else if(uno.whosTurn == 2){
                db.collection("games").document(gameId).update("player2CardsDeck", FieldValue.arrayUnion(card.getColor()+"-"+card.getType()));
                playerCards.add(card);
                adapter.notifyDataSetChanged();
                docRef.update("whosTurn",3);
            }
        }
    }

    private void Draw4CardsFromDeck(DocumentSnapshot value,ArrayList<Card> playerCards,DeckAdapter adapter){
        //get floor deck
        String arrayOfCards = value.get("floorCardsDeck").toString();
        String result = arrayOfCards.replaceAll("[\\p{Ps}\\p{Pe}]", "");
        Log.d(TAG, "onSuccess: floorCardsDeck result:"+ result );
        String[] items = result.split(", ");
        //get index 0-3 card string
        //convert to card
        for (int i = 0; i<4;i++) {
            if (items.length > 4) {
                Card card = new Card(items[0]);
                DocumentReference docRef = db.collection("games").document(gameId);
                docRef.update("floorCardsDeck", FieldValue.arrayRemove(card.getColor() + "-" + card.getType()));
                playerCards.add(card);
                if (value.get("whosTurn").equals(1)) {
                    db.collection("games").document(gameId).update("player2CardsDeck", FieldValue.arrayUnion(card.getColor()+"-"+card.getType()));
                    playerCards.add(card);
                    docRef.update("whosTurn",2);
                } else if (value.get("whosTurn").equals(2)) {
                    db.collection("games").document(gameId).update("player1CardsDeck", FieldValue.arrayUnion(card.getColor()+"-"+card.getType()));
                    playerCards.add(card);
                    docRef.update("whosTurn",1);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean isGameFinished(){
        if(player1Cards.size() == 0 || player2Cards.size() == 0) {
            DocumentReference docRef = db.collection("games").document(gameId);
            docRef.update("status", "FINISHED");
            return true;
        }else {
            return false;
        }
    }



}