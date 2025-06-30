package com.example.ingredientscanner.data.remote.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface NutritionixAPI {
    @Headers({
            "x-app-id: 82270b18",
            "x-app-key: b755caaae7cbc0f55fb7f69eb3a2009f"
    })
    @GET("v2/search/item")
    Call<NutritionixResponse> searchProduct(@Query("upc") String upc);
}
