package com.example.app;

public class FoodOptionFactory {
    public static FoodOption createFoodOption(String type) {
        switch (type) {
            case "Koi":
                return new Koi();
            case "Wokhey":
                return new Wokhey();
            case "Mac":
                return new Mac();
            default:
                throw new IllegalArgumentException("Unknown food option type: " + type);
        }
    }
}
