package com.example.ingredientscanner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class KcalLimitActivity extends AppCompatActivity {
    private EditText kcalLimitInput;
    private Button saveKcalLimitBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kcal_limit_activity);

        kcalLimitInput = findViewById(R.id.kcalLimitInput);
        saveKcalLimitBtn = findViewById(R.id.saveKcalLimitBtn);

        // Load previously saved limit if any
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        float savedLimit = prefs.getFloat("kcal_limit", 0);
        if (savedLimit > 0) {
            kcalLimitInput.setText(String.valueOf(savedLimit));
        }

        // Save the new limit when button is clicked
        saveKcalLimitBtn.setOnClickListener(v -> {
            String input = kcalLimitInput.getText().toString().trim();

            if (input.isEmpty()) {
                Toast.makeText(this, "Please enter a valid kcal limit.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float limit = Float.parseFloat(input);
                if (limit <= 0) throw new NumberFormatException();

                // Save the set limit to "kcal_limit" in SharedPreferences
                prefs.edit().putFloat("kcal_limit", limit).apply();
                Toast.makeText(this, "Calorie limit saved!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to previous screen
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid number format.", Toast.LENGTH_SHORT).show();
            }

        });

    }
}
