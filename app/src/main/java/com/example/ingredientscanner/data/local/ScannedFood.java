package com.example.ingredientscanner.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scanned_foods")
public class ScannedFood {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String productName;
    private String ingredients;
    private float calories;
    private long scanTime;

    // Required no-arg constructor for Room
    public ScannedFood() {}

    // Constructor
    public ScannedFood(String productName, String ingredients, float calories, long scanTime) {
        this.productName = productName;
        this.ingredients = ingredients;
        this.calories = calories;
        this.scanTime = scanTime;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public long getScanTime() {
        return scanTime;
    }

    public void setScanTime(long scanTime) {
        this.scanTime = scanTime;
    }

    @Override
    public String toString() {
        return "ScannedFood{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", calories=" + calories +
                ", scanTime=" + scanTime +
                '}';
    }
}
