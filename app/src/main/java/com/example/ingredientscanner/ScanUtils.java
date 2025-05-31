package com.example.ingredientscanner;

import java.util.ArrayList;
import java.util.List;
public class ScanUtils {
    public static boolean isOverCaloriesLimit(float scannedCalories, float userLimit) {
        return scannedCalories > userLimit;
    }

    public static List<String> getDetectedAllergens(String ingredients, String keywordsCsv) {
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
