package com.example.uno_test.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.example.uno_test.R;
import com.example.uno_test.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
private static final String TAG = "demo";

    private AppBarConfiguration appBarConfiguration;
    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Main");
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            navController.navigate(R.id.lobby_to_auth_action);
        }


//        Player player1 = new Player("Graham");
//        Player player2 = new Player("Tom");
//
//        Board gameBoard = new Board();
//        gameBoard.setPlayers(player1,player2);
//        gameBoard.StartGame(player1,player2);
//        Log.d(TAG, "GameBoard : "+ gameBoard.toString());

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

}