package com.example.ingredientscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity that checks if the scanned product exceeds the user's daily calorie limit
 * and if it contains any allergens set by the user.
 */
public class ScanAnalysisActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_kcal_limit);

        TextView kcalCheckResultTextView = findViewById(R.id.kcalCheckResult);

        // Retrieve the scanned product data from intent
        float scannedCalories = getIntent().getFloatExtra("scannedCalories", 0);
        String scannedIngredientList = getIntent().getStringExtra("ingredients");

        // Load the user-defined preferences
        SharedPreferences preferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        float userCalorieLimit = preferences.getFloat("kcal_limit", 0);
        String userAllergyKeywords = preferences.getString("userAllergyKeywords", "");

        // Prepare userAllergyKeywords checking
        boolean containsAllergen = false;
        String[] allergyKeywords = userAllergyKeywords.split(",");
        StringBuilder detectedAllergens = new StringBuilder();

        // Calories check logic
        if (scannedCalories > userCalorieLimit) {
            kcalCheckResultTextView.setText("⚠️ Warning: This product exceeds your daily kcal limit!\n" +
                    "----------------------------------------\n" +
                    "Scanned: " + scannedCalories + " kcal\nLimit: " + userCalorieLimit + " kcal");
            kcalCheckResultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            kcalCheckResultTextView.setText("✅ This product is within your set kcal limit.\n" +
                    "----------------------------------------\n" +
                    "Scanned: " + scannedCalories + " kcal\nLimit: " + userCalorieLimit + " kcal");
            kcalCheckResultTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        // Allergy keyword check using basic NLP keyword matching
        if (scannedIngredientList != null) {
            String normalizedIngredients = scannedIngredientList.toLowerCase();

            for (String keyword: allergyKeywords) {
                String normalizedKeyword= keyword.trim().toLowerCase();
                if (!normalizedKeyword.isEmpty() && normalizedIngredients.contains(normalizedKeyword)) {
                    containsAllergen = true;
                    detectedAllergens.append(normalizedKeyword).append("\n");
                }
            }
        }

        if (containsAllergen) {
            Toast.makeText(this,
                    "Allergy Warning!\nThis product may contain:\n" + detectedAllergens,
                    Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "No allergens detected in this product.", Toast.LENGTH_LONG).show();
        }

        // Handle "View Details" button click
        Button viewDetailsButton = findViewById(R.id.viewDetailsButton);
        viewDetailsButton.setOnClickListener(v -> {
            Intent detailIntent = new Intent(this, ProductDetailActivity.class);
            detailIntent.putExtras(getIntent()); // forward all product details
            startActivity(detailIntent);
        });
    }
}
