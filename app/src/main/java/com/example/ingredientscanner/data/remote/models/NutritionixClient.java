package com.example.ingredientscanner.data.remote.models;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NutritionixClient {
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.nutritionix.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
