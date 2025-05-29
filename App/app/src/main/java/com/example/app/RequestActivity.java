package com.example.app;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RequestActivity extends Activity {
    int order;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request);
        Navigation.setupNavigation(this);
        LinearLayout container = findViewById(R.id.scrollwheel);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_email = currentUser.getEmail();

        // Get reference to the TextView
        TextView TextUser = findViewById(R.id.textUser);
        TextUser.setText("Logged in as: " + user_email);

        //TODO:THEY CREATE A BUNCH OF ORDERS IN THE SCROLLVIEW
        db.collection("Orders").whereEqualTo("Status","Order placed").orderBy("Order ID")
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Log.d("ORDER FOUND","Retrieving now");
                            String orderId = doc.get("Order ID").toString();


                            String foodStore = doc.getString("Food Store");
                            String meet_Location = doc.getString("Meeting Location");
                            String dist =  doc.getString("Distance");
                            //Add new view
                            View itemView = getLayoutInflater().inflate(R.layout.request_item, container, false);
                            ImageView img = itemView.findViewById(R.id.Store_image);
                            TextView Storename = itemView.findViewById(R.id.StoreTitle);
                            TextView distance = itemView.findViewById(R.id.distance);
                            TextView malllocation = itemView.findViewById(R.id.mall_location);
                            TextView meetlocation = itemView.findViewById(R.id.meet_up_location);
                            TextView orderview = itemView.findViewById(R.id.orderID);
                            Log.d("ORDER FOUND","Initialized widgets");

                            Storename.setText("Store: " + foodStore);
                            distance.setText("Distance: " + dist);
                            meetlocation.setText("Meet up Location: " + meet_Location);
                            orderview.setText(orderId);


                            switch(foodStore){
                                case "KOI":
                                    Koi koi = new Koi();
                                    img.setImageResource(koi.DrawableImage().get(0));
                                    malllocation.setText("Mall: "+ koi.getmalllocation());
                                    break;
                                case "MAC DONALD":
                                    Mac mc = new Mac();
                                    img.setImageResource(mc.DrawableImage().get(0));
                                    malllocation.setText("Mall: "+ mc.getmalllocation());
                                    break;
                                case "Wok Hey":
                                    Wokhey wok = new Wokhey();
                                    img.setImageResource(wok.DrawableImage().get(0));
                                    malllocation.setText("Mall: "+ wok.getmalllocation());
                                    break;
                            }

                            container.addView(itemView);
                            Log.d("ORDER FOUND","Added view");


                            itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String orderId = orderview.getText().toString();
                                    Long order = Long.parseLong(orderId);
                                    Log.d("TEST", String.valueOf(order));
                                    Intent intent = new Intent(RequestActivity.this, PickupActivity.class);
                                    intent.putExtra("Selected Order", order);
                                    startActivity(intent);
                                }
                            });
                            Log.d("Order Info", "Order ID: " + orderId + ", Store: " + foodStore);
                            // You can populate a UI list or do something with this data
                        }
                    } else {
                        Log.d("Orders", "No orders with 'Order placed' status.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Orders", "Failed to fetch orders: " + e.getMessage());
                });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //TODO:STRINGS AND INTEGERS ARE FOR TESTING BECAUSE ORDER DOESN'T EXIST


}