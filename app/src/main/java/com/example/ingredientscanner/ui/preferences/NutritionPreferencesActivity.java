package com.example.ingredientscanner.ui.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ingredientscanner.R;
import com.example.ingredientscanner.ui.history.HistoryActivity;
import com.example.ingredientscanner.ui.main.MainActivity;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class NutritionPreferencesActivity extends AppCompatActivity {
    private EditText caloriesLimitEditText;

    private EditText allergenEditText;

    private ChipGroup allergenChipGroup;
    // private AutoCompleteTextView allergyInputField;
    private Button savePreferencesButton;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_preferences);

        caloriesLimitEditText = findViewById(R.id.kcalLimitInput);
        allergenEditText = findViewById(R.id.allergenEditText);
        allergenChipGroup = findViewById(R.id.allergenChipGroup);
        savePreferencesButton = findViewById(R.id.saveBtn);

        // Retrieve previously saved user preferences for daily calorie limit and allergen keywords
        // These values are used to prepopulate the input fields so the user can view or modify them
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        float savedCalorieLimit = getSafeFloatPreference("kcal_limit", 0f);
        String savedAllergyKeywords = sharedPreferences.getString("allergy", "");

        // Pre-fill calorie limit
        if (savedCalorieLimit > 0) {
            caloriesLimitEditText.setText(String.valueOf(savedCalorieLimit));
        }

        // Pre-fill allergens as chips
        if (!savedAllergyKeywords.isEmpty()) {
            String[] keywords = savedAllergyKeywords.split(",");
            for (String keyword : keywords) {
                addChip(keyword.trim());
            }
        }

        allergenEditText.setOnEditorActionListener((v, actionId, event) -> {
            String input = allergenEditText.getText().toString().trim();
            if (!input.isEmpty()) {
                addChip(input);
                allergenEditText.setText("");
            }
            return true;
        });

        // Save preferences on button click
        savePreferencesButton.setOnClickListener(v -> {
            String caloriesLimitText = caloriesLimitEditText.getText().toString().trim();

            // Validate both inputs before saving
            if (!isValidCalorieLimit(caloriesLimitText)) {
                caloriesLimitEditText.setError("Please enter a valid daily limit (1,000–6,000 kcal)");
                return;
            }

            // Collect allergens from ChipGroup
            StringBuilder allergens = new StringBuilder();
            for (int i = 0; i < allergenChipGroup.getChildCount(); i++) {
                Chip chip = (Chip) allergenChipGroup.getChildAt(i);
                allergens.append(chip.getText().toString()).append(",");
            }
            if (allergens.length() > 0) {
                allergens.setLength(allergens.length() - 1); // remove last comma
            }
            String allergyInputText = allergens.toString();

            try {
                float calorieLimit = Float.parseFloat(caloriesLimitText);

                sharedPreferences.edit()
                        .putFloat("kcal_limit", calorieLimit)
                        .putString("allergy", allergyInputText)
                        .apply();

                Toast.makeText(this, "Preferences saved successfully.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Unexpected error parsing calories.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates the daily calorie limit input.
     *
     * @param input The raw string input from the EditText field.
     * @return true if the input is non-empty, numeric, and within 1000–6000 kcal/day.
     */
    private boolean isValidCalorieLimit(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        try {
            float calorieLimit = Float.parseFloat(input.trim());
            return calorieLimit > 0 && calorieLimit <= 6000;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates the allergy keyword input.
     *
     * @param input The raw string input from the allergy input field.
     * @return true if the input is non-empty and contains only letters, commas, and spaces.
     */
    private boolean isValidAllergyInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return false;
        }

        // Regex: allows letters (a-z, A-Z), commas, and spaces
        return input.trim().matches("^[a-zA-Z,\\s]+$");
    }

    private void addChip(String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(v -> allergenChipGroup.removeView(chip));
        allergenChipGroup.addView(chip);
    }

    private float getSafeFloatPreference(String key, float defaultValue) {
        try {
            return sharedPreferences.getFloat(key, defaultValue);
        } catch (ClassCastException e) {
            // Handle previously stored int
            return (float) sharedPreferences.getInt(key, (int) defaultValue);
        }
    }
}
