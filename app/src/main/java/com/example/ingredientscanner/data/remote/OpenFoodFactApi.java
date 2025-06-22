package com.example.ingredientscanner.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;

import com.example.ingredientscanner.data.remote.models.IngredientResponse;

public interface OpenFoodFactApi {
    @GET("ingredients.json?page=1&page_size=1000")
    Call<IngredientResponse> getAllIngredients();
}
