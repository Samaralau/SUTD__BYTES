package com.example.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Mac implements FoodOption{
    @Override
    public String getShopName() {
        return "MAC DONALD";
    }

    @Override
    public String getmalllocation() {
        return "Changi City Point";
    }

    @Override
    public double getdistance() {
        return 2.6;
    }

    @Override
    public String getstorelocation() {
        return "#01-10";
    }

    @Override
    public List<String> getcat() {
        return Arrays.asList("Burger","Sides");
    }

    @Override
    public List<String> getFoodNames() {
        return Arrays.asList("Fish Burger", "French Fries");
    }

    @Override
    public List<Double> getPrices() {
        return Arrays.asList(3.5, 2.2);
    }

    @Override
    public List<Integer> getQuantities() {
        return Arrays.asList(1, 1, 1, 1, 1);
    }

    @Override
    public List<Integer> DrawableImage() {
        return Arrays.asList(R.drawable.mc_donald,R.drawable.macfishburger,R.drawable.macfrenchfries);
    }
}
