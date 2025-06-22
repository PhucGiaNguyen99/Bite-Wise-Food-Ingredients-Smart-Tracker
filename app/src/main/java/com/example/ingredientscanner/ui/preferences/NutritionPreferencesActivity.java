package com.example.ingredientscanner.ui.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ingredientscanner.R;
import com.example.ingredientscanner.data.remote.models.IngredientResponse;
import com.example.ingredientscanner.data.remote.models.IngredientSuggestion;
import com.example.ingredientscanner.data.remote.OpenFoodFactApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NutritionPreferencesActivity extends AppCompatActivity {
    private List<IngredientSuggestion> cachedIngredients = new ArrayList<>();

    private EditText caloriesLimitEditText;
    private AutoCompleteTextView allergyInputField;
    private Button savePreferencesButton;

    private OpenFoodFactApi api;
    private SharedPreferences sharedPreferences;
    private final long API_DELAY = 500; // 0.5s debounce delay
    private Timer debounceTimer = new Timer();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_preferences);

        // Initialize Retrofit
        // Use Gson library to convert JSON responses to Java objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(OpenFoodFactApi.class);

        fetchFullIngredientList();

        // Find views
        caloriesLimitEditText = findViewById(R.id.kcalLimitInput);
        allergyInputField = findViewById(R.id.allergenInput);
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
            allergyInputField.setText(savedAllergyKeywords);
        }

        // Debounced text input listener
        allergyInputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Timer oldTimer = debounceTimer;
                debounceTimer = new Timer();
                oldTimer.cancel();
                debounceTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> filterLocalSuggestion(s.toString()));
                    }
                }, API_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle allergen selection
        allergyInputField.setOnItemClickListener((parent, view, position, id) -> {
            String selectedAllergen = (String) parent.getItemAtPosition(position);
            allergyInputField.setText(selectedAllergen); // Ensure clean selection
            preferences.edit().putString("allergy", selectedAllergen).apply();
            Toast.makeText(this, "Saved allergen: " + selectedAllergen, Toast.LENGTH_SHORT).show();
        });

        // Save preferences on button click
        savePreferencesButton.setOnClickListener(v -> {
            String caloriesLimitText = this.caloriesLimitEditText.getText().toString().trim();
            String selectedAllergy = this.allergyInputField.getText().toString().trim();

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
                        .putString("allergy", selectedAllergy)
                        .apply();

                Toast.makeText(this, "Preferences saved successfully.", Toast.LENGTH_SHORT).show();
                // finish(); // Close the activity and return

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format for calorie limit.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper function to retrieve the ingredient list retrieved from the API
    private void fetchFullIngredientList() {
        api.getAllIngredients().enqueue(new Callback<IngredientResponse>() {
            @Override
            public void onResponse(Call<IngredientResponse> call, Response<IngredientResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().ingredients != null) {
                    cachedIngredients = response.body().ingredients;
                } else {
                    Toast.makeText(NutritionPreferencesActivity.this, "Failed to load ingredient list.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<IngredientResponse> call, Throwable t) {
                Toast.makeText(NutritionPreferencesActivity.this, "API error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Suggestion fetch logic
    private void filterLocalSuggestion(String query) {
        if (query.length() < 2 || cachedIngredients.isEmpty()) return;

        List<String> filtered = new ArrayList<>();
        for (IngredientSuggestion item : cachedIngredients) {
            if (item.text != null && item.text.toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item.text);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, filtered);
        allergyInputField.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (!isFinishing() && !isDestroyed()) {
            allergyInputField.showDropDown();
        }
    }
}
