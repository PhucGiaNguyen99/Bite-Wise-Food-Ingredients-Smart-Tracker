package com.example.ingredientscanner.data.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NutritionixResponse {
    @SerializedName("foods")
    private List<Product> foods;

    public List<Product> getFoods() {
        return foods;
    }

    public static class Product {
        @SerializedName("food_name")
        public String name;

        @SerializedName("brand_name")
        public String brand;

        @SerializedName("nf_calories")
        public float calories;

        @SerializedName("nf_ingredient_statement")
        public String ingredients;
    }
}
