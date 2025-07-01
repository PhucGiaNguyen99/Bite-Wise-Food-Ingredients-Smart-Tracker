package com.example.ingredientscanner.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scanned_foods")
public class ScannedFood {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String brand;
    public String ingredients;
    public float calories;
    public long timestamp;

    public ScannedFood(String name, String brand, String ingredients, float calories, long timestamp) {
        this.name = name;
        this.brand = brand;
        this.ingredients = ingredients;
        this.calories = calories;
        this.timestamp = timestamp;
    }
}

