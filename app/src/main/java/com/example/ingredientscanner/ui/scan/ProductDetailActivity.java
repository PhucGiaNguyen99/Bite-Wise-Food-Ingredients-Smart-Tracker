package com.example.ingredientscanner.ui.scan;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ingredientscanner.R;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView productNameView, brandView, ingredientsView, caloriesView;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save all the necessary values into the bundle
        outState.putString("productName", productNameView.getText().toString());
        outState.putString("brand", brandView.getText().toString());
        outState.putString("ingredients", ingredientsView.getText().toString());
        outState.putString("calories", caloriesView.getText().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize UI components for displaying product information
        productNameView = findViewById(R.id.productNameView);
        brandView = findViewById(R.id.brandView);
        ingredientsView = findViewById(R.id.ingredientsView);
        caloriesView = findViewById(R.id.caloriesView);

        String name, brand, ingredients, calories;

        if (savedInstanceState != null) {
            // Restore data from previously saved instance state
            name = savedInstanceState.getString("productName");
            brand = savedInstanceState.getString("brand");
            ingredients = savedInstanceState.getString("ingredients");
            calories = savedInstanceState.getString("calories");
        } else {
            // Retrieve product details sent from MainActivity through Intent extras
            name = getIntent().getStringExtra("productName");
            brand = getIntent().getStringExtra("brand");
            ingredients = getIntent().getStringExtra("ingredients");
            calories = getIntent().getStringExtra("calories");
        }

        // Display the extracted product data in the corresponding views
        productNameView.setText(name);
        brandView.setText(brand);
        ingredientsView.setText(ingredients);
        caloriesView.setText(calories);
    }


}
