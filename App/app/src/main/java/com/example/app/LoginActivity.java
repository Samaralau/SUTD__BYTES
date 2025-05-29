package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;



import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity {
    private FirebaseAuth auth;
    private EditText loginEmail, loginPassword;
    private TextView signupRedirectText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login); // Set the layout for this activity
        auth = FirebaseAuth.getInstance();
        loginEmail = findViewById(R.id.emailEditText);
        loginButton = findViewById(R.id.loginButton);
        signupRedirectText = findViewById(R.id.signUpButton);
        loginPassword = findViewById(R.id.passwordEditText);  // Initialize loginPassword



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get email and password entered by the user
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();

                // Use Firebase Authentication to sign in
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Login is successful
                                // Show a success toast
                                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                // Start the HomePageActivity
                                //Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                //Order page
                                Intent intent = new Intent(LoginActivity.this, OrderActivity.class);
                                startActivity(intent);

                                // Optional: Close the current login activity
                                finish();
                            } else {
                                // Login failed
                                // Show a failure toast
                                Toast.makeText(LoginActivity.this, "Login unsuccessful. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        // Set OnClickListener for the signupRedirectText (TextView)
        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to the RegistrationActivity (Sign up page)
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}



