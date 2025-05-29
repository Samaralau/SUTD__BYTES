package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RedeemActivity extends Activity {

    private int remain_value;
    private int current_value;
    private String Voucher;
    private Long selVoucher;
    private Long ShortVoucher;
    private Long MedVoucher;
    private Long LongVoucher;
    DocumentReference userRef;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_redeem);
        Navigation.setupNavigation(this);
        Button short_trip = findViewById(R.id.btn_short_redeem);
        Button med_trip = findViewById(R.id.btn_medium_redeem);
        Button long_trip = findViewById(R.id.btn_long_redeem);
        TextView sel_trip = findViewById(R.id.selected_trip_text);
        TextView remaining = findViewById(R.id.remaining_point_balance);
        TextView current_points = findViewById(R.id.current_point_balance);
        Button btn_confirm = findViewById(R.id.btn_confirm_redeem);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_email = currentUser.getEmail();
        db.collection("users")
                .whereEqualTo("email", user_email)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        DocumentSnapshot doc = snapshot.getDocuments().get(0);
                        Long points = doc.getLong("Points");
                        current_points.setText("Current Points: " + points);
                        current_value = points.intValue();
                        ShortVoucher = doc.getLong("Short Trip Voucher");
                        MedVoucher = doc.getLong("Medium Trip Voucher");
                        LongVoucher = doc.getLong("Long Trip Voucher");
                        userRef = doc.getReference();
                    }
                });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> updates = new HashMap<>();
                updates.put(Voucher,selVoucher+1);
                updates.put("Points",remain_value);
                userRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Voucher", "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Voucher", "Error updating document", e);
                            }
                        });
                Intent intent = new Intent(RedeemActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
        short_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sel_trip.setText("Selected Trip: Short Distance");
                Voucher = "Short Trip Voucher";
                selVoucher = ShortVoucher;
                remain_value = current_value - 20;
                if(remain_value< 0){
                    remaining.setText("Not sufficient points");
                }
                else{
                    remaining.setText("Remaining Points: " + String.valueOf(remain_value));
                }
            }
        });
        med_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sel_trip.setText("Selected Trip: Medium Distance");
                Voucher = "Medium Trip Voucher";
                selVoucher = MedVoucher;
                remain_value = current_value - 50;
                if(remain_value< 0){
                    remaining.setText("Not sufficient points");
                }
                else{
                    remaining.setText("Remaining Points: " + String.valueOf(remain_value));
                }

            }
        });
        long_trip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sel_trip.setText("Selected Trip: Long Distance");
                Voucher = "Long Trip Voucher";
                selVoucher = LongVoucher;
                remain_value = current_value - 100;
                if(remain_value< 0){
                    remaining.setText("Not sufficient points");
                }
                else{
                    remaining.setText("Remaining Points: " + String.valueOf(remain_value));
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}