package com.example.ingredientscanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NutritionPreferencesActivity extends AppCompatActivity {
    private EditText caloriesLimitEditText, allergyKeywordsEditText;
    private Button savePreferencesButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kcal_limit_activity);

        caloriesLimitEditText = findViewById(R.id.kcalLimitInput);
        allergyKeywordsEditText = findViewById(R.id.allergyInput);
        savePreferencesButton = findViewById(R.id.saveBtn);

        // Load previously saved values from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        float savedCalorieLimit = preferences.getFloat("kcal_limit", 0);
        String savedAllergyKeywords = preferences.getString("allergy", "");

        // Populate the input fields if values exist
        if (savedCalorieLimit > 0) {
            caloriesLimitEditText.setText(String.valueOf(savedCalorieLimit));
        }
        if (!savedAllergyKeywords.isEmpty()) {
            allergyKeywordsEditText.setText(savedAllergyKeywords);
        }

        // Save preferences on button click
        savePreferencesButton.setOnClickListener(v -> {
            String caloriesLimitText = this.caloriesLimitEditText.getText().toString().trim();
            String allergyKeywordsText = this.allergyKeywordsEditText.getText().toString().trim();

            if (caloriesLimitText.isEmpty()) {
                Toast.makeText(this, "Please enter a valid kcal limit.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float calorieLimit = Float.parseFloat(caloriesLimitText);
                if (calorieLimit <= 0) throw new NumberFormatException();

                // Save the calorie limit and allergy keywords
                preferences.edit()
                        .putFloat("kcal_limit", calorieLimit)
                        .putString("allergy", allergyKeywordsText)
                        .apply();

                Toast.makeText(this, "Preferences saved successfully.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity and return

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format for calorie limit.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
