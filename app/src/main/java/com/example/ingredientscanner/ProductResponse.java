package com.example.ingredientscanner;

import com.google.gson.annotations.SerializedName;

public class ProductResponse {
    public Product product;

    public static class Product {
        public String product_name;
        public String brands;
        public String ingredients_text;
        public Nutriments nutriments;
    }

    public static class Nutriments {
        // Add more field names for kcal retrieved from the API
        @SerializedName("energy-kcal")
        public Float energyKcal;

        @SerializedName("energy-kcal_100g")
        public Float energyKcal100g;

        @SerializedName("energy-kcal_serving")
        public Float energyKcalServing;
    }
}
