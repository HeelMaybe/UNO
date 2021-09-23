package com.example.uno_test.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.uno_test.R;
import com.example.uno_test.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Authentication");

        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_auth);
    }
}
