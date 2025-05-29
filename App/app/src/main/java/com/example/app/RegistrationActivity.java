package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends Activity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private ImageButton passwordToggleButton, confirmPasswordToggleButton;
    private TextView loginLink;

    // FirebaseAuth instance
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        passwordToggleButton = findViewById(R.id.passwordToggleButton);
        confirmPasswordToggleButton = findViewById(R.id.confirmPasswordToggleButton);
        loginLink = findViewById(R.id.loginLink);

        // Set up listeners
        passwordToggleButton.setOnClickListener(v -> togglePasswordVisibility(passwordEditText, passwordToggleButton));
        confirmPasswordToggleButton.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordEditText, confirmPasswordToggleButton));

        registerButton.setOnClickListener(v -> handleRegistration());

        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Method to toggle password visibility
    private void togglePasswordVisibility(EditText editText, ImageButton toggleButton) {
        if (editText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleButton.setImageResource(R.drawable.show_password_eye); // Use an eye icon to show password
        } else {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleButton.setImageResource(R.drawable.hide_password_eye); // Use an eye-off icon to hide password
        }
    }

    // Method to handle registration
    private void handleRegistration() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Register user with Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Create a HashMap for user data
                        Map<String, Object> newuser = new HashMap<>();
                        newuser.put("email", email);
                        newuser.put("password", password);
                        newuser.put("Points", 0);
                        newuser.put("Short Request done:", 0);
                        newuser.put("Medium Request done:", 0);
                        newuser.put("Long Request done:", 0);
                        newuser.put("Short Trip Voucher", 0);
                        newuser.put("Medium Trip Voucher:", 0);
                        newuser.put("Long Trip Voucher", 0);
                        db.collection("users")  // creates a "users" collection
                                .add(newuser);
                        // If registration is successful, sign in the user
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(RegistrationActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();

                        // Navigate to login or home activity
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // If registration fails, show an error message
                        Toast.makeText(RegistrationActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
