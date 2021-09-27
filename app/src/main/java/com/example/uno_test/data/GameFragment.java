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
import com.example.uno_test.databinding.FragmentGameBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
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
            }
        });
        rvCards.setAdapter(rvaCards);

    }

    private void getCards(){
        cards.clear();
            db.collection("games")
                    .document(gameId)
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Log.d(TAG, "onSuccess: player1CardsDeck:"+ documentSnapshot.get("player1CardsDeck").toString() );
                    String arrayOfCards = documentSnapshot.get("player1CardsDeck").toString();
                    String result = arrayOfCards.replaceAll("[(){}]","");
                    Log.d(TAG, "onSuccess: player1CardsDeck result:"+ result );

                    String[] items = result.split(", ");
                    for (int i = 0; i<items.length;i++){
                        Card card = new Card(items[i]);
                        cards.add(card);
                    }
                    rvaCards.notifyDataSetChanged();
                }
            });



    }
}