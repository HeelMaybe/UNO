package com.example.uno_test.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.uno_test.Adapters.GameAdapter;
import com.example.uno_test.R;
import com.example.uno_test.data.Game;
import com.example.uno_test.databinding.FragmentLobbyBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class LobbyFragment extends Fragment {

    private FragmentLobbyBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Game> games = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLobbyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
        getGames();

        binding.btnAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LobbyFragment.this)
                        .navigate(R.id.lobby_to_create_action);
            }
        });

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                NavHostFragment.findNavController(LobbyFragment.this)
                        .navigate(R.id.lobby_to_auth_action);
            }
        });

        binding.buttonGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(LobbyFragment.this)
                        .navigate(R.id.lobby2game_action);
            }
        });

    }

    private void setupRecyclerView() {
        recyclerView = binding.recyclerViewGames;
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        mAdapter = new GameAdapter(games, new GameAdapter.IGameRow() {
            @Override
            public void onGameSelected(Game game) {

                Bundle args = new Bundle();
                args.putString(GameFragment.GAME_KEY, game.id);
                args.putString(GameFragment.GAME_TITLE, game.title);

                    TODO: NavHostFragment.findNavController(LobbyFragment.this)
                                        .navigate(R.id.lobby2game_action, args);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void getGames() {
        db.collection("games").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("CRDebug", "Listen failed.", e);
                    return;
                }

                games.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Game game = doc.toObject(Game.class);
                    game.setId(doc.getId());
                    games.add(game);
                }
//                games.sort(new Comparator<Game>() {
//                    @Override
//                    public int compare(Game o1, Game o2) {
//                        return o2.createdAt.compareTo(o1.createdAt);
//                    }
//                });
                mAdapter.notifyDataSetChanged();
            }
        });
    }


}
