package com.example.ingredientscanner;

import com.example.ingredientscanner.APIModels.IngredientResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenFoodFactApi {
    @GET("ingredients.json")
    Call<IngredientResponse> getIngredients(@Query("search") String query);
}
