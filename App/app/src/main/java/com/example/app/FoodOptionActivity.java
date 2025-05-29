package com.example.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;

public class FoodOptionActivity extends Activity {
    int quantity1 = 0;
    int quantity2 = 0;
    double price1 = 0;
    double price2 = 0;
    FoodOption option;
    CartItem cartItem;
    ///List<CartItem> cart;
    ArrayList<CartItem> cart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.koi_order);
        Navigation.setupNavigation(this);

        TextView user_view = findViewById(R.id.textUser);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_email = currentUser.getEmail();
        user_view.setText("Logged in as: " + user_email);

        option = (FoodOption) getIntent().getSerializableExtra("food_option");

        if (option != null) {
            //<-- changing of store info according to food selected-->
            TextView storeTitle = findViewById(R.id.StoreTitle);
            TextView malllocation = findViewById(R.id.mall_location);
            TextView storelocation = findViewById(R.id.store_location);

            storeTitle.setText(option.getShopName());
            malllocation.setText(option.getmalllocation());
            storelocation.setText(option.getstorelocation());

            //<-- changing of food categories -->
            TextView category1 = findViewById(R.id.food1_cat);
            TextView category2 = findViewById(R.id.food2_cat);

            category1.setText(option.getcat().get(0));
            category2.setText(option.getcat().get(1));

            //<-- changing of food options and prices-->
            TextView food_1 = findViewById(R.id.food1);
            TextView food_2 = findViewById(R.id.food2);
            TextView food1price = findViewById(R.id.food1_price);
            TextView food2price = findViewById(R.id.food2_price);

            food_1.setText(option.getFoodNames().get(0));
            food_2.setText(option.getFoodNames().get(1));
            food1price.setText(String.format("$%.2f", option.getPrices().get(0)));
            food2price.setText(String.format("$%.2f", option.getPrices().get(1)));

            //<-- changing of pictures -->
            ImageView storepic = findViewById(R.id.store_pic);
            ImageView food1 = findViewById(R.id.option1_pic);
            ImageView food2 = findViewById(R.id.option2_pic);

            storepic.setImageResource(option.DrawableImage().get(0));
            food1.setImageResource(option.DrawableImage().get(1));
            food2.setImageResource(option.DrawableImage().get(2));

            //Buttons to add item into cart
            Button add1 = findViewById(R.id.addtocart1);
            add1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FoodOptionActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();

                    quantity1++;
                    price1 += option.getPrices().get(0);

                    CartItem cartItem = new CartItem(
                            option.getShopName(),
                            option.getFoodNames().get(0),
                            quantity1,
                            price1,
                            option.getdistance()
                    );
                    cart.add(cartItem);
                }
            });
            //add button 2
            Button add2 = findViewById(R.id.addtocart2);
            add2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(FoodOptionActivity.this, "Added to cart!", Toast.LENGTH_SHORT).show();

                    quantity2++;
                    price2 += option.getPrices().get(1);

                    CartItem cartItem2 = new CartItem(
                            option.getShopName(),
                            option.getFoodNames().get(1),
                            quantity2,
                            price2,
                            option.getdistance()
                    );
                    cart.add(cartItem2);
                }
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
        // go to cartActivity page
        Button cartbutton = findViewById(R.id.gotocart);
        cartbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FoodOptionActivity.this, CartActivity.class);
                intent.putExtra("cart_items", cart);
                startActivity(intent);
            }
        });

    }
}