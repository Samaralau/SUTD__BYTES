package com.example.app;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String storeName;
    private String foodName;
    private int quantity;
    private double distance;
    private double totalPrice;

    // Constructor
    public CartItem(String storeName, String foodName, int quantity, double totalPrice, double distance) {
        this.storeName = storeName;
        this.foodName = foodName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.distance = distance;
    }

    // Getters and setters
    public String getStoreName() {
        return storeName;
    }

    public double getDistance(){return distance;}

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
