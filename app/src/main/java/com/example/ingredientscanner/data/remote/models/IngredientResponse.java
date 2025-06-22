package com.example.ingredientscanner.data.remote.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IngredientResponse {
    @SerializedName("ingredients")
    public List<IngredientSuggestion> ingredients;
}
