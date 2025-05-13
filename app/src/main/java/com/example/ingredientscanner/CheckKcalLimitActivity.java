package com.example.ingredientscanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CheckKcalLimitActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_kcal_limit);

        TextView resultText = findViewById(R.id.kcalCheckResult);

        float scannedCalories = getIntent().getFloatExtra("scannedCalories", 0);

        SharedPreferences prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        float limit = prefs.getFloat("kcal_limit", 0);


        if (scannedCalories > limit) {
            resultText.setText("⚠️ Warning: This product exceeds your daily kcal limit!\n" +
                    "----------------------------------------\n" +
                    "Scanned: " + scannedCalories + " kcal\nLimit: " + limit + " kcal");
            resultText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            resultText.setText("✅ This product is within your set kcal limit.\n\nScanned: " +
                    "----------------------------------------\n" +
                    scannedCalories + " kcal\nLimit: " + limit + " kcal");
            resultText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        Button viewDetailsButton = findViewById(R.id.viewDetailsButton);
        viewDetailsButton.setOnClickListener(v -> {
            Intent detailIntent = new Intent(this, ProductDetailActivity.class);
            detailIntent.putExtras(getIntent()); // forward all product details
            startActivity(detailIntent);
        });
    }
}
