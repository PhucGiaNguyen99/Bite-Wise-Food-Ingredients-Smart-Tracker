package com.example.ingredientscanner.ui.scan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ingredientscanner.R;

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

        // Calories check logic
        List<String> detectedAllergens = getDetectedAllergens(scannedIngredientList, userAllergyKeywords);
        if (!detectedAllergens.isEmpty()) {
            StringBuilder msg = new StringBuilder("Allergy Warning!\nThis product may contain:\n");
            for (String allergen : detectedAllergens) {
                msg.append(allergen).append("\n");
            }
            Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
        } else {
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

    // Helper function to check if over calorie limit
    private boolean isOverCaloriesLimit(float scannedCalories, float userLimt) {
        return scannedCalories > userLimt;
    }

    // Helper function to find allergens from ingredient list
    private List<String> getDetectedAllergens(String ingredients, String keywordsCsv) {
        List<String> detected = new ArrayList<>();
        if (ingredients == null || keywordsCsv == null) return detected;

        String[] keywords = keywordsCsv.split(",");
        String normalizedIngredients = ingredients.toLowerCase();

        for (String keyword : keywords) {
            String trimmed = keyword.trim().toLowerCase();
            if (!trimmed.isEmpty() && normalizedIngredients.contains(trimmed)) {
                detected.add(trimmed);
            }
        }
        return detected;
    }
}
