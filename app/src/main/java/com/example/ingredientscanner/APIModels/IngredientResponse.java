package com.example.ingredientscanner.APIModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponse {
    @SerializedName("ingredients")
    public List<IngredientSuggestion> ingredients;
}
