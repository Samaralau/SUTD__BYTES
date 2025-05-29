package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends Activity {

    private FirebaseAuth auth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        Navigation.setupNavigation(this);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_email = currentUser.getEmail();
        // Get reference to the TextView
        TextView TextUser = findViewById(R.id.textUser);
        TextUser.setText("Logged in as: " + user_email);
        TextView requesttaken = findViewById(R.id.requests_taken);
        TextView short_trip = findViewById(R.id.short_trips);
        TextView med_trip = findViewById(R.id.medium_trips);
        TextView long_trip = findViewById(R.id.long_trips);
        TextView totalpoints = findViewById(R.id.total_points);
        Button btn_redeem = findViewById(R.id.redeem_button);
        //fetch
        db.collection("users")
                .whereEqualTo("email", user_email)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        DocumentSnapshot doc = snapshot.getDocuments().get(0);
                        Long longCount = doc.getLong("Long Request done:");
                        Long medCount = doc.getLong("Medium Request done:");
                        Long shortCount = doc.getLong("Short Request done:");
                        Long points = doc.getLong("Points");

                        Log.d("UserProfile", "Long Count: " + longCount);
                        Log.d("UserProfile", "Medium Count: " + medCount);
                        Log.d("UserProfile", "Short Count: " + shortCount);
                        Log.d("UserProfile", "Points: " + points);

                        // Display values with labels
                        long_trip.setText("Long Distance Trips: " + longCount);
                        med_trip.setText("Medium Distance Trips: " + medCount);
                        short_trip.setText("Short Distance Trips: " + shortCount);
                        totalpoints.setText("Total Points: " + points);

                        // Use raw values for calculation
                        int total = (int)(longCount + medCount + shortCount);
                        requesttaken.setText("Total request taken: " + total);
                    }
                });

        btn_redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, RedeemActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}