package com.example.ingredientscanner;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScannedFoodDao {

    @Insert
    void insert(ScannedFood food);

    @Query("SELECT * FROM scanned_foods ORDER BY scanTime DESC")
    List<ScannedFood> getAllScans();

    @Query("DELETE FROM scanned_foods")
    void deleteAll();
}
