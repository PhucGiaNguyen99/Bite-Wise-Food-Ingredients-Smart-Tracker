package com.example.ingredientscanner.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface ScannedFoodDao {
    @Insert
    void insert(ScannedFood food);

    @Query("SELECT * FROM scanned_foods ORDER BY timestamp DESC")
    List<ScannedFood> getAllFoods();

    @Query("SELECT * FROM scanned_foods WHERE DATE(timestamp / 1000, 'unixepoch') = DATE('now')")
    List<ScannedFood> getTodayScans();
}

