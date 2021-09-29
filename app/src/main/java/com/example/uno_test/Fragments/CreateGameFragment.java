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
import com.example.uno_test.data.Game;
import com.example.uno_test.data.GameFragment;
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


public class CreateGameFragment extends Fragment {
    private static final String TAG = "demo";

    private FragmentCreateGameBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String creatorName;
    String ownerId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        binding.btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUser();
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
                        //FirebaseAuth.getInstance().getCurrentUser().getUid()

                        String title = binding.etTitle.getText().toString();
                        DocumentReference docRef = db.collection("games").document();
                        Game newGame = new Game(docRef.getId(), creatorName,ownerId,title);
                        newGame.generateStartDeck();

                        docRef.set(newGame)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Bundle args = new Bundle();
                                        args.putString(GameFragment.GAME_KEY, newGame.gameId);
                                        args.putString(GameFragment.GAME_TITLE, newGame.title);

                                        NavHostFragment.findNavController(CreateGameFragment.this)
                                                .navigate(R.id.create2game_action, args);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Error adding document", e);
                                        Snackbar.make(getView(), "Error creating the game.", Snackbar.LENGTH_LONG).show();
                                    }
                                });

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}