package com.example.app;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Wokhey implements FoodOption {
    @Override
    public String getShopName() {
        return "Wok Hey";
    }

    @Override
    public String getmalllocation() {
        return "Changi City Point";
    }

    @Override
    public double getdistance() {
        return 3.3;
    }

    @Override
    public String getstorelocation() {
        return "#B1-07";
    }

    @Override
    public List<String> getcat() {
        return Arrays.asList("Rice","Noodles");
    }

    @Override
    public List<String> getFoodNames() {
        return Arrays.asList("Egg Fried Rice", "Fried Udon");
    }

    @Override
    public List<Double> getPrices() {
        return Arrays.asList(5.5, 6.0);
    }

    @Override
    public List<Integer> getQuantities() {
        return Arrays.asList(1, 1, 1, 1, 1);
    }

    @Override
    public List<Integer> DrawableImage() {
        return Arrays.asList(R.drawable.wok_hey,R.drawable.wokheyfriedrice,R.drawable.wokheyudon);
    }

}
