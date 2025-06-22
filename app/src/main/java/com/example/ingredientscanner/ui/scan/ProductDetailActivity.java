package com.example.ingredientscanner.ui.scan;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ingredientscanner.R;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView productNameView, brandView, ingredientsView, caloriesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productNameView = findViewById(R.id.productNameView);
        brandView = findViewById(R.id.brandView);
        ingredientsView = findViewById(R.id.ingredientsView);
        caloriesView = findViewById(R.id.caloriesView);

        // Receive product data
        String name = getIntent().getStringExtra("productName");
        String brand = getIntent().getStringExtra("brand");
        String ingredients = getIntent().getStringExtra("ingredients");
        String calories = getIntent().getStringExtra("calories");

        productNameView.setText(name);
        brandView.setText(brand);
        ingredientsView.setText(ingredients);
        caloriesView.setText(calories);
    }
}
