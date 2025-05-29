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

public class ReviewActivity extends Activity {

    double deliveryFee;
    String DistanceType;
    String distance;
    private Long SelDone;
    private Long ord;
    private Long ShortDone;
    private Long MedDone;
    private Long LongDone;
    int current_value;
    int gain_points;
    List<String> foodchoice;
    List<Double> pricing;
    List<Integer> qty;
    Double Totalprice;
    DocumentReference orderRef;
    DocumentReference userRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Navigation.setupNavigation(this);

        TextView role_view = findViewById(R.id.role_label);
        Button confirm_btn = findViewById(R.id.confirm_btn);
        TextView store_view = findViewById(R.id.StoreTitle);
        TextView mall_view = findViewById(R.id.mall_location);
        TextView storeloc_view = findViewById(R.id.store_location);
        TextView meet_view = findViewById(R.id.meet_up_location);
        TextView email_view = findViewById(R.id.textUser);
        ImageView img = findViewById(R.id.store_pic);
        TextView totalprice = findViewById(R.id.totalcost);
        LinearLayout container = findViewById(R.id.orderListLayout);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Role logic from intent
        String role = getIntent().getStringExtra("role");
        ord = getIntent().getLongExtra("orderID", -1);
        Log.d("ORDER", String.valueOf(ord));
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_email = currentUser.getEmail();
        //Users Database
        db.collection("users")
                .whereEqualTo("email", user_email)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        DocumentSnapshot doc = snapshot.getDocuments().get(0);
                        Long points = doc.getLong("Points");
                        current_value = points.intValue();
                        ShortDone = (Long) doc.get("Short Request Done:");
                        MedDone = (Long) doc.get("Medium Request Done:");
                        LongDone = (Long) doc.get("Long Request Done:");
                        userRef = doc.getReference();
                        Log.d("Tag", String.valueOf(userRef));
                    }
                });

        //Order Database
        db.collection("Orders").whereEqualTo("Order ID", ord)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Log.d("ORDER FOUND", "Retrieving now");
                            //String orderId = doc.get("Order ID").toString();
                            //ord = Long.parseLong(orderId);
                            String status = doc.getString("Status");
                            distance = doc.getString("Distance");
                            orderRef = doc.getReference();
                            Log.d("TAG", String.valueOf(orderRef));
                            //Retrieve request details
                            String foodStore = doc.getString("Food Store");
                            String meet_Location = doc.getString("Meeting Location");
                            String dist = doc.getString("Distance");
                            foodchoice = (List<String>) doc.get("food choice");
                            pricing = (List<Double>) doc.get("Price");
                            qty = (List<Integer>) doc.get("Quantity");
                            Totalprice = doc.getDouble("Total Price");

                            //Getting values for Updating database
                            DistanceType = getDistanceType(distance);
                            SelDone = getDone(DistanceType);
                            gain_points = getPoints(DistanceType);

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
                    } else {
                        Log.d("Orders", "No orders with 'Order placed' status.");
                    }
                });

        if (role.equals("picker")) {
            role_view.setText("You are the Picker");
            confirm_btn.setText("Delivery Completed");

            confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.collection("Orders")
                            .whereEqualTo("Order ID", ord)
                            .get()
                            .addOnSuccessListener(snapshot -> {
                                if (!snapshot.isEmpty()) {
                                    Log.d("TAG", "Completed order found. Proceeding to update user.");
                                    Log.d("TAG", String.valueOf(SelDone));
                                    Log.d("TAG", DistanceType);
                                    Log.d("TAG", String.valueOf((current_value + gain_points)));
                                    //DocumentReference userRef = db.collection("users").document(String.valueOf(userRef));
                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put(DistanceType, 1);
                                    updates.put("Points", (current_value + gain_points));

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
                                            });} else {
                                    Log.d("TAG", "No completed order found. Cannot update user points.");
                                    Toast.makeText(ReviewActivity.this, "Order is not marked as Completed yet!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("TAG", "Failed to check order status", e);
                                Toast.makeText(ReviewActivity.this, "Failed to check order status.", Toast.LENGTH_SHORT).show();
                            });

                    Intent intent = new Intent(ReviewActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            role_view.setText("You are the Requester");
            confirm_btn.setText("Order Received");
            confirm_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DocumentReference OrderRef = db.collection("Orders").document(String.valueOf(orderRef));
                    // Set the status to Received
                    orderRef.update("Status", "Completed")
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

                    Intent intent = new Intent(ReviewActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    Toast.makeText(ReviewActivity.this, "Request completed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public String getDistanceType(String distance){
        String DistanceType;
        if(distance.equals("Short distance")){
            DistanceType = "Short Request done:";

        }
        else if(distance.equals("Medium distance")){
            DistanceType = "Medium Request done:";
        }
        else{
            DistanceType = "Long Request done:";
        }
        return DistanceType;
    }
    public Long getDone(String distanceType){
        Long seldone;
        if(distanceType.equals("Short Request done:")){
            seldone = ShortDone;
        }
        else if(distanceType.equals("Medium Request done:")){
            seldone = MedDone;
        }
        else{
            seldone = LongDone;
        }
        return seldone;
    }
    public int getPoints(String distanceType){
        int getpoint;
        if(distanceType.equals("Short Request done:")){
            getpoint = 3;
        }
        else if(distanceType.equals("Medium Request done:")){
            getpoint = 5;
        }
        else{
            getpoint = 7;
        }
        return getpoint;
    }
}
