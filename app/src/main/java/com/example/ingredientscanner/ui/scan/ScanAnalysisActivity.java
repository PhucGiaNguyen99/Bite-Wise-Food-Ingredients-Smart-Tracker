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

        // Retrieve scanned product data from Intent
        int scannedCalories = getIntent().getIntExtra("calories", 0);
        String scannedIngredientList = getIntent().getStringExtra("ingredients");

        // Retrieve user preferences
        SharedPreferences preferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        float userCalorieLimit = preferences.getFloat("kcal_limit", 0f);
        String userAllergyKeywords = preferences.getString("allergy", "");

        // Perform health and safety checks
        boolean isOverLimit = isOverCaloriesLimit(scannedCalories, userCalorieLimit);
        List<String> allergens = getDetectedAllergens(scannedIngredientList, userAllergyKeywords);

        // Display result
        displayAnalysisResult(kcalCheckResultTextView, isOverLimit, userCalorieLimit, allergens);

        // View product detail
        Button viewDetailsButton = findViewById(R.id.viewDetailsButton);
        viewDetailsButton.setOnClickListener(v -> {
            Intent detailIntent = new Intent(this, ProductDetailActivity.class);
            detailIntent.putExtras(getIntent());
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

    private void displayAnalysisResult(TextView resultView, boolean overCalorie, float limit, List<String> allergens) {
        StringBuilder result = new StringBuilder();

        if (overCalorie) {
            result.append("⚠️ This product exceeds your calorie limit of ")
                    .append(limit)
                    .append(" kcal.\n");
        }

        if (!allergens.isEmpty()) {
            result.append("🚨 Allergy Warning: Contains ")
                    .append(String.join(", ", allergens))
                    .append(".\n");
        }

        if (result.length() == 0) {
            result.append("✅ Product is safe based on your preferences.");
        }

        resultView.setText(result.toString());
        Toast.makeText(this, result.toString(), Toast.LENGTH_LONG).show();
    }

}
