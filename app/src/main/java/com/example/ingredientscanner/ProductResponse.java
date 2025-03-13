package com.example.ingredientscanner;

public class ProductResponse {
    public Product product;

    public static class Product {
        public String product_name;
        public String brands;
        public String ingredients_text;
        public Nutriments nutriments;
    }

    public static class Nutriments {
        public float energy_kcal;
    }
}
