package com.example.ingredientscanner;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scanned_foods")
public class ScannedFood {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String productName;
    public String ingredients;
    public float calories;
    public long scanTime;

    public ScannedFood(String productName, String ingredients, float calories, long scanTime) {
        this.productName = productName;
        this.ingredients = ingredients;
        this.calories = calories;
        this.scanTime = scanTime;
    }
}
