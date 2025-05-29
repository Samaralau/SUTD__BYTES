package com.example.app;

import android.graphics.drawable.Drawable;
import android.widget.Button;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Koi implements FoodOption{
    @Override
    public String getShopName() {
        return "KOI";
    }

    @Override
    public String getmalllocation() {
        return "Changi City Point";
    }

    @Override
    public double getdistance() {
        return 5.1;
    }

    @Override
    public String getstorelocation() {
        return "#B1-18";
    }

    @Override
    public List<String> getcat() {
        return Arrays.asList("Milk Tea","Black Tea");
    }

    @Override
    public List<String> getFoodNames() {
        return Arrays.asList("Jumbo Milk Tea", "Lychee Black Tea");
    }

    @Override
    public List<Double> getPrices() {
        return Arrays.asList(5.8, 4.9);
    }

    @Override
    public List<Integer> getQuantities() {
        return Arrays.asList(1, 1, 1, 1, 1);
    }

    @Override
    public List<Integer> DrawableImage() {
        return Arrays.asList(R.drawable.jumbomilktea_removebg_preview,R.drawable.jumbomilktea_removebg_preview,R.drawable.lycheeblacktea);
    }

}
