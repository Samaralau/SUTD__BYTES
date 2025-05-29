package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends Activity implements Serializable {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int quantity1 = 1;
    double deliveryFee;
    double grandTotal = 0.0;
    int orderID;
    double pricing;
    String redeem_distance;
    List<Double> pricelist = new ArrayList<>();
    List<Integer> quantitylist = new ArrayList<>();
    List<String> foodlist = new ArrayList<>();
    List<CartItem> cartItems;
    List<Button> addButtons = new ArrayList<>();
    List<Button> removeButtons = new ArrayList<>();

    Spinner voucherSpinner;
    double discount = 0.0;
    double discountview;
    String selectedVoucher = "No Voucher";
    List<String> voucherList = new ArrayList<>();
    Map<String, Double> voucherMap = new HashMap<>();

    private final String sharedPrefFile = "com.example.android.cartsaver";
    public static final String KEY = "MyKey";
    SharedPreferences mPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        Navigation.setupNavigation(this);
        cartItems = (List<CartItem>) getIntent().getSerializableExtra("cart_items"); // receive intent
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Reference the scrollable container inside ScrollView
        LinearLayout cartOrderLayout = findViewById(R.id.cart_order);
        TextView storename = findViewById(R.id.store_name_cart);
        TextView distance = findViewById(R.id.reedeming_distance_cart);
        TextView user_view = findViewById(R.id.textUser);
        TextView role = findViewById(R.id.role_label);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_email = currentUser.getEmail();
        user_view.setText("Logged in as: " + user_email);
        role.setText("Role: Requester");


        voucherSpinner = findViewById(R.id.voucherSpinner);

        //load from shared pref first!!
        if (cartItems == null) {
            String cartJson = mPreferences.getString(KEY, null);
            if (cartJson != null) {
                Gson gson = new Gson();
                CartItem[] cartArray = gson.fromJson(cartJson, CartItem[].class);
                cartItems = new ArrayList<>(Arrays.asList(cartArray));
            }
        }

        if (cartItems == null){
            cartItems = new ArrayList<>();
            redeem_distance = "NIL";
            deliveryFee = 0.0;
            distance.setText("No items in cart");
        }
        else{
            redeem_distance = Deliveryfee.redeemDistance(cartItems.get(0).getDistance());
            deliveryFee = Deliveryfee.getDeliveryfee(cartItems.get(0).getDistance());
            loadVouchers(user_email, deliveryFee, distance);
        }

        for (int i = 0; i < cartItems.size(); i++) {
            CartItem item = cartItems.get(i);
            final int index = i;
            View cartItemView = getLayoutInflater().inflate(R.layout.item_cart, null);
            // Add the inflated cart item view into the scroll container
            cartOrderLayout.addView(cartItemView);

            Button add1 = cartItemView.findViewById(R.id.add_1);
            Button remove1 = cartItemView.findViewById(R.id.remove_1);
            TextView quantity = cartItemView.findViewById(R.id.food_quantity_cart);
            quantity.setText(String.valueOf(quantity1));

            TextView foodname = cartItemView.findViewById(R.id.food_name_cart);
            TextView deliveryprice = findViewById(R.id.delivery_fee_cart);
            TextView price = cartItemView.findViewById(R.id.food_price_cart);
            TextView totalprice = findViewById(R.id.total_price_cart);

            addButtons.add(add1);
            removeButtons.add(remove1);

            storename.setText(item.getStoreName());
            foodname.setText(item.getFoodName());
            deliveryprice.setText(String.format("$%.2f", deliveryFee));
            distance.setText("");
            price.setText("$" + String.format("%.2f", item.getTotalPrice()));
            pricelist.add(item.getTotalPrice());
            foodlist.add(item.getFoodName());


            final int[] itemQuantity = {item.getQuantity()};
            Log.d("TAG", Arrays.toString(itemQuantity));
            recalculateTotal(totalprice);
            quantitylist.add(item.getQuantity());
            Log.d("Quantity",quantitylist.toString());


            // Increase quantity
            add1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemQuantity[0]++;
                    item.setQuantity(itemQuantity[0]);
                    quantity.setText(itemQuantity[0] + "x");
                    pricing = itemQuantity[0] * item.getTotalPrice();
                    quantitylist.set(index,itemQuantity[0]);
                    Log.d("TAG", Arrays.toString(itemQuantity));
                    Log.d("Quantity",quantitylist.toString());
                    price.setText("$" +String.format("%.2f",pricing));
                    recalculateTotal(totalprice);

                }
            });

            //decrease quantity
            remove1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemQuantity[0] > 1) {
                        itemQuantity[0]--;
                        item.setQuantity(itemQuantity[0]);
                        quantity.setText(itemQuantity[0] + "x");
                        pricelist.remove(pricing);
                        foodlist.remove(item.getFoodName());
                        pricing = itemQuantity[0] * item.getTotalPrice();
                        price.setText("$" +String.format("%.2f",pricing));
                        }

                    else {

                        itemQuantity[0] = 0;
                        item.setQuantity(itemQuantity[0]);

                        cartItemView.setVisibility(View.GONE);

                        recalculateTotal(totalprice);
                    }
                }
            });

            //confirm button
            Button confirmButton = findViewById(R.id.confirm_cart);
            EditText location = findViewById(R.id.meet_up_location);
            confirmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locationtext = location.getText().toString();
                    if (locationtext.isEmpty()) {
                        Toast.makeText(CartActivity.this, "Enter your location!", Toast.LENGTH_SHORT).show();
                    } else if (cartItems == null || itemQuantity[0] == 0) {
                        Toast.makeText(CartActivity.this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CartActivity.this, "Request successfully sent!", Toast.LENGTH_LONG).show();

                        confirmButton.setVisibility(View.INVISIBLE);
                        for (Button a : addButtons) a.setVisibility(View.GONE);
                        for (Button b : removeButtons) b.setVisibility(View.GONE);

                        // Make location field uneditable
                        DocumentReference counterRef = db.collection("counters").document("order_counter");

                        db.runTransaction(transaction -> {
                            DocumentSnapshot snapshot = transaction.get(counterRef);
                            Long currentCount = snapshot.getLong("count");
                            if (currentCount == null) currentCount = 0L;
                            long newOrderId = currentCount + 1;
                            orderID = Integer.parseInt(String.valueOf(newOrderId));

                            // Update counter
                            transaction.update(counterRef, "count", newOrderId);

                            // Prepare the new order
                            Map<String, Object> newOrder = new HashMap<>();
                            newOrder.put("Order ID", newOrderId);
                            newOrder.put("Status", "Order placed");
                            newOrder.put("Requester", user_email);
                            newOrder.put("Picker", "NIL");
                            newOrder.put("food choice", foodlist);
                            newOrder.put("Price", pricelist);
                            newOrder.put("Total Price", grandTotal);
                            newOrder.put("Food Store", storename.getText().toString());
                            newOrder.put("Meeting Location", location.getText().toString());
                            newOrder.put("Quantity", quantitylist);
                            newOrder.put("Distance", redeem_distance);
                            newOrder.put("Food Image", RetrieveLocation.getImage(storename.getText().toString()));

                            // Save order using new ID as the document ID
                            DocumentReference newOrderRef = db.collection("Orders").document(String.valueOf(newOrderId));
                            transaction.set(newOrderRef, newOrder);

                            return null;
                        }).addOnSuccessListener(aVoid -> {
                        Toast.makeText(getApplicationContext(), "Order placed with ID " + orderID + "!", Toast.LENGTH_SHORT).show();

                        // Shared Preferences
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        Gson gson = new Gson();
                        String finaljson = gson.toJson(cartItems);
                        preferencesEditor.putString("confirmed_cart", finaljson);
                        preferencesEditor.putString("meetup_location", location.getText().toString());
                        preferencesEditor.remove(KEY);
                        preferencesEditor.apply();

                        cartItems.clear();

                        Intent intent = new Intent(CartActivity.this, ReviewActivity.class);
                        intent.putExtra("role", "requester");
                        intent.putExtra("orderID", orderID);
                        startActivity(intent);
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to place order: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });



                }

                }
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void recalculateTotal(TextView totalView) {
        double subtotal = 0.0;
        for (CartItem item : cartItems) {
            if (item.getQuantity() > 0) {
                subtotal += item.getTotalPrice() * item.getQuantity();
            }
        }
        double finalTotal = subtotal + deliveryFee - discount;
        if (finalTotal < 0){
            finalTotal = 0;
        }
        grandTotal = finalTotal;
        totalView.setText(String.format("$%.2f", finalTotal));
    }

    private void loadVouchers(String user,Double deliveryFee,TextView distance) {
        db.collection("users").whereEqualTo("email", user)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.isEmpty()) {
                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            voucherList.add("No Voucher"); // default option
                            voucherMap.put("No Voucher", 0.0);

                            Long shortTrip = doc.getLong("Short Trip Voucher");
                            Long mediumTrip = doc.getLong("Medium Trip Voucher");
                            Long longTrip = doc.getLong("Long Trip Voucher");

                            if (shortTrip != null && shortTrip > 0 && deliveryFee  == 1.5) {
                                voucherList.add("Short Trip Voucher");
                                voucherMap.put("Short Trip Voucher", 1.50); // Example discount
                            }
                            if (mediumTrip != null && mediumTrip > 0 && deliveryFee == 3.0) {
                                voucherList.add("Medium Trip Voucher");
                                voucherMap.put("Medium Trip Voucher", 3.00);
                            }
                            if (longTrip != null && longTrip > 0 && deliveryFee == 5.0) {
                                voucherList.add("Long Trip Voucher");
                                voucherMap.put("Long Trip Voucher", 5.00);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(CartActivity.this, android.R.layout.simple_spinner_item, voucherList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            voucherSpinner.setAdapter(adapter);

                            voucherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    selectedVoucher = parent.getItemAtPosition(position).toString();
                                    discount = voucherMap.getOrDefault(selectedVoucher, 0.0);
                                    discountview = discount;
                                    distance.setText(redeem_distance + "( - $" + discountview + " )");
                                    recalculateTotal(findViewById(R.id.total_price_cart));
                                    Log.d("Voucher","Apply voucher");
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    selectedVoucher = "No Voucher";
                                    discount = 0.0;
                                    discountview = discount;
                                    distance.setText("NIL");
                                }
                            });
                        }
                    } else {
                        Log.d("Voucher", "Could not be added or used");
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

        Gson gson = new Gson();
        String cartJson = gson.toJson(cartItems);

        // Save to SharedPreferences
        preferencesEditor.putString(KEY, cartJson);
        preferencesEditor.apply();
    }


}

