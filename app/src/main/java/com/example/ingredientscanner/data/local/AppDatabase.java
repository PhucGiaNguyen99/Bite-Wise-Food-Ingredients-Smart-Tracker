package com.example.ingredientscanner.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ScannedFood.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScannedFoodDao scannedFoodDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "bitewise_db").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}

