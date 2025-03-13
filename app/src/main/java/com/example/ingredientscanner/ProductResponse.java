package com.example.ingredientscanner;

public class ProductResponse {
    public Product product;

    public class Product {
        public String product_name;
        public String brands;
        public String ingredients_text;
        public Nutriments nutriments;
    }

    public class Nutriments {
        public float energy_kcal;
    }
}
