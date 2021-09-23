package com.example.uno_test.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.uno_test.R;
import com.example.uno_test.databinding.FragmentLoginBinding;
import com.example.uno_test.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    private FirebaseAuth mAuth;
    final private String TAG = "demo";
    FragmentRegisterBinding binding;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSignIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextEmailAddress2.getText().toString();
                String password = binding.editTextPassword2.getText().toString();
                String name = binding.editTextName.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Name", Toast.LENGTH_SHORT).show();
                }else if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "onComplete: Signed up Successfully");
                                            //TODO: save name of user to firestore with viewmodels
                                        NavHostFragment.findNavController(RegisterFragment.this)
                                                .navigate(R.id.register_signIn_action);


                                    } else {
                                        Log.d(TAG, "onComplete: Error!!!");
                                        Log.d(TAG, "onComplete: " + task.getException().getMessage());
                                        Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }


        });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(RegisterFragment.this)
                        .navigate(R.id.cancel_signup_action);
            }
        });
    }
}


