package com.example.uno_test.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.uno_test.R;
import com.example.uno_test.databinding.FragmentLobbyBinding;
import com.google.firebase.auth.FirebaseAuth;


public class LobbyFragment extends Fragment {
    private FragmentLobbyBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                NavHostFragment.findNavController(LobbyFragment.this)
                        .navigate(R.id.lobby_to_auth_action);
            }
        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.action_logout:
//                mAuth.signOut();
//                NavHostFragment.findNavController(LobbyFragment.this)
//                        .navigate(R.id.lobby_to_auth_action);
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}