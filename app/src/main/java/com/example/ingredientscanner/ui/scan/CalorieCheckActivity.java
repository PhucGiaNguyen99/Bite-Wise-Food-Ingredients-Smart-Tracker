package com.example.ingredientscanner.ui.scan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ingredientscanner.R;
import com.example.ingredientscanner.data.local.AppDatabase;
import com.example.ingredientscanner.data.local.ScannedFood;

import java.util.List;
import java.util.concurrent.Executors;

public class CalorieCheckActivity extends AppCompatActivity {

    private TextView calorieSummaryText;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_check);

        calorieSummaryText = findViewById(R.id.calorieSummaryText);
        preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        checkTodayCalories();
    }

    private void checkTodayCalories() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ScannedFood> todayFoods = AppDatabase.getInstance(this).scannedFoodDao().getTodayScans();

            float totalCalories = 0;
            for (ScannedFood food : todayFoods) {
                totalCalories += food.calories;
            }

            float kcalLimit = preferences.getFloat("kcal_limit", 0);

            String resultMessage = "You have consumed " + totalCalories + " kcal today.\n";
            if (kcalLimit > 0) {
                resultMessage += totalCalories <= kcalLimit
                        ? "✅ Within your daily calorie limit of " + kcalLimit + " kcal!"
                        : "❌ You exceeded your daily limit of " + kcalLimit + " kcal.";
            } else {
                resultMessage += "No calorie limit set.";
            }

            String finalMessage = resultMessage;
            runOnUiThread(() -> calorieSummaryText.setText(finalMessage));


        });
    }
}

