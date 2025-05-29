package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import java.util.List;
import java.util.Map;


public class PickupActivity extends Activity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    Long neworder;
    List<String> foodchoice;
    List<Double> pricing;
    List<Integer> qty;
    Double Totalprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickup);
        Navigation.setupNavigation(this); // Setup bottom navigation bar

        //Get current user's email from Firebase Authentication
        String user_email = currentUser.getEmail();

        //Initialize widgets
        TextView order_view = findViewById(R.id.orderID);
        TextView store_view = findViewById(R.id.StoreTitle);
        TextView mall_view = findViewById(R.id.mall_location);
        TextView storeloc_view = findViewById(R.id.store_location);
        TextView meet_view = findViewById(R.id.meet_up_location);
        TextView email_view = findViewById(R.id.textUser);
        ImageView img = findViewById(R.id.store_pic);
        TextView totalprice = findViewById(R.id.totalcost);
        LinearLayout container = findViewById(R.id.orderListLayout);
        Button btn_accept = findViewById(R.id.btn_accept);

        email_view.setText("Logged in as: " + user_email);

        // Retrieve the order object passed from RequestActivity
        Bundle extras = getIntent().getExtras();
        neworder = extras.getLong("Selected Order");
        Log.d("ORDER REQUEST", String.valueOf(neworder));

        //Retrieve data from Database
        db.collection("Orders").whereEqualTo("Order ID", neworder)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Log.d("ORDER REQUEST", "Retrieving now");
                            //Retrieve request details
                            String foodStore = doc.getString("Food Store");
                            String meet_Location = doc.getString("Meeting Location");
                            String dist = doc.getString("Distance");
                            //food_img = (List<Integer>) doc.get("Food Image");
                            foodchoice = (List<String>) doc.get("food choice");
                            pricing = (List<Double>) doc.get("Price");
                            qty = (List<Integer>) doc.get("Quantity");
                            Totalprice = doc.getDouble("Total Price");
                            //Set basic widget
                            order_view.setText("Picked Request: ID:" + String.valueOf(neworder) + " ,Distance Type: " + dist);
                            store_view.setText(foodStore);

                            Log.d("ORDER REQUEST", "Retrieve location");
                            RetrieveLocation.setimgloc(img,mall_view,storeloc_view,foodStore);
                            meet_view.setText("Meet-up Point "+ meet_Location);
                            //Linear Layout stack
                            for (int i = 0; i < foodchoice.size(); i++) {
                                View itemView = getLayoutInflater().inflate(R.layout.item_order, null);
                                ImageView item_img = itemView.findViewById(R.id.item_image);
                                TextView food_view = itemView.findViewById(R.id.item_name);
                                TextView qty_view = itemView.findViewById(R.id.item_quantity);
                                TextView price_view = itemView.findViewById(R.id.item_price);
                                food_view.setText(foodchoice.get(i));
                                qty_view.setText("Qty: " + qty.get(i));
                                price_view.setText(String.format("$%.2f", pricing.get(i)));
                                //item_img.setImageResource(food_img.get(0));
                                container.addView(itemView);
                            }
                            // Set total cost text
                            totalprice.setText("Total cost: $"+ Totalprice);
                        }
                    }else {
                        Log.d("Orders", "No orders with the specified Order ID");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Orders", "Failed to fetch order: " + e.getMessage());
                });

                // Dynamically add item in scroll view


        //Handle accept button click
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update Order in Database
                DocumentReference OrderRef = db.collection("Orders").document(String.valueOf(neworder));
                Map<String, Object> updates = new HashMap<>();
                updates.put("Status", "In Progress");
                updates.put("Picker", user_email); // Add more as needed
                // Set the status to in progress
                OrderRef.update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error updating document", e);
                            }
                        });

                Intent intent = new Intent(PickupActivity.this, ReviewActivity.class);
                intent.putExtra("role", "picker");
                intent.putExtra("orderID", neworder);
                startActivity(intent);
                Toast.makeText(PickupActivity.this, "Request accepted", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}