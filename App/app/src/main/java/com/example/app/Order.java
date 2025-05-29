package com.example.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Order implements FoodOption {
    private String shopName;
    private String mallLocation;
    private String storeLocation;
    private List<String> items;
    private List<Integer> quantities;
    private List<Double> prices;
    private double distance;
    private int image;

    public Order(String shopName, String mallLocation, String storeLocation,
                 List<String> items, List<Integer> quantities, List<Double> prices,Integer Image, double distance) {
        this.shopName = shopName;
        this.mallLocation = mallLocation;
        this.storeLocation = storeLocation;
        this.items = items;
        this.quantities = quantities;
        this.prices = prices;
        this.image = Image;
        this.distance = distance;
    }

    // Getters only — we don’t want this object getting ideas of grandeur
    public String getShopName() { return shopName; }
    public String getmalllocation() { return mallLocation; }

    @Override
    public double getdistance() {
        return distance;
    }

    public String getstorelocation() { return storeLocation; }

    @Override
    public List<String> getcat() {
        return Collections.emptyList();
    }

    public List<String> getFoodNames() { return items; }
    public List<Integer> getQuantities() { return quantities; }

    @Override
    public List<Integer> DrawableImage() {
        return Arrays.asList(this.image);
    }

    public List<Double> getPrices() { return prices; }
    //public double getTotalPrice() { return totalPrice; }
}