package com.example.ingredientscanner.ui.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ingredientscanner.R;

public class NutritionPreferencesActivity extends AppCompatActivity {
    private EditText caloriesLimitEditText, allergyInputEditText;
    // private AutoCompleteTextView allergyInputField;
    private Button savePreferencesButton;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_preferences);

        caloriesLimitEditText = findViewById(R.id.kcalLimitInput);
        allergyInputEditText = findViewById(R.id.allergenInput);
        savePreferencesButton = findViewById(R.id.saveBtn);

        // Retrieve previously saved user preferences for daily calorie limit and allergen keywords
        // These values are used to prepopulate the input fields so the user can view or modify them
        SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int savedCalorieLimit = preferences.getInt("kcal_limit", 0);
        String savedAllergyKeywords = preferences.getString("allergy", "");

        if (savedCalorieLimit > 0) {
            caloriesLimitEditText.setText(String.valueOf(savedCalorieLimit));
        }
        if (!savedAllergyKeywords.isEmpty()) {
            allergyInputEditText.setText(savedAllergyKeywords);
        }

        // Save preferences on button click
        savePreferencesButton.setOnClickListener(v -> {
            String caloriesLimitText = caloriesLimitEditText.getText().toString().trim();
            String allergyInputText = allergyInputEditText.getText().toString().trim();

            // Validate both inputs before saving
            if (!isValidCalorieLimit(caloriesLimitText)) {
                caloriesLimitEditText.setError("Please enter a valid daily limit (1,000–6,000 kcal)");
                return;
            }

            if (!isValidAllergyInput(allergyInputText)) {
                allergyInputEditText.setError("Use only letters, commas, and spaces for allergens");
                return;
            }

            try {
                int calorieLimit = Integer.parseInt(caloriesLimitText);

                preferences.edit()
                        .putInt("kcal_limit", calorieLimit)
                        .putString("allergy", allergyInputText)
                        .apply();

                Toast.makeText(this, "Preferences saved successfully.", Toast.LENGTH_SHORT).show();
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
            int calorieLimit = Integer.parseInt(input.trim());
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
}
