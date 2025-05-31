package com.example.ingredientscanner;

import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ScanUtilsTest {
    @Test
    public void testIsOverCaloriesLimit() {
        assertTrue(ScanUtils.isOverCaloriesLimit(500, 300));
        assertFalse(ScanUtils.isOverCaloriesLimit(250, 300));
        assertFalse(ScanUtils.isOverCaloriesLimit(300, 300));
    }

    @Test
    public void testGetDetectedAllergens_MatchesFound() {
        String ingredients = "milk, sugar, wheat, soy";
        String keywords = "milk, nuts, soy";
        List<String> result = ScanUtils.getDetectedAllergens(ingredients, keywords);
        assertEquals(2, result.size());
        assertTrue(result.contains("milk"));
        assertTrue(result.contains("soy"));
    }

    @Test
    public void testGetDetectedAllergens_NoMatches() {
        String ingredients = "rice, corn, water";
        String keywords = "milk, soy";
        List<String> result = ScanUtils.getDetectedAllergens(ingredients, keywords);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetDetectedAllergens_EmptyInput() {
        List<String> result1 = ScanUtils.getDetectedAllergens(null, "milk");
        List<String> result2 = ScanUtils.getDetectedAllergens("milk", null);
        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
    }
}
