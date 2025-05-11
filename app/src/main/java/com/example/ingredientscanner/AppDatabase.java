package com.example.ingredientscanner;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ScannedFood.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{
    private static volatile AppDatabase INSTANCE;

    public abstract ScannedFoodDao scannedFoodDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "scanned_foods_db"
                    ).allowMainThreadQueries().build(); // Use .build() only for basic cases!
                }
            }
        }
        return INSTANCE;
    }
}
