package com.example.ingredientscanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenFoodFactsAPITest {
    @Mock
    private OpenFoodFactAPI apiMock;

    @Mock
    private Call<ProductResponse> mockCall;

    private String testBarcode = "3017620422003";   // For Nutella

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchProductSuccess() {
        ProductResponse fakeResponse = new ProductResponse();
        fakeResponse.product = new ProductResponse.Product();
        fakeResponse.product.product_name = "Nutella";
        fakeResponse.product.brands = "Ferrero";
        fakeResponse.product.ingredients_text = "Sugar, Palm Oil, Hazelnuts, Cocoa";
        fakeResponse.product.nutriments = new ProductResponse.Nutriments();
        fakeResponse.product.nutriments.energy_kcal = 530;

        when(apiMock.getProductByBarcode(testBarcode)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<ProductResponse> callback = invocation.getArgument(0);
            callback.onResponse(mockCall, Response.success(fakeResponse));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        apiMock.getProductByBarcode(testBarcode).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                assertEquals("Nutella", response.body().product.product_name);
                assertEquals("Ferrero", response.body().product.brands);
                assertEquals("Sugar, Palm Oil, Hazelnuts, Cocoa", response.body().product.ingredients_text);
                assertEquals(530, response.body().product.nutriments.energy_kcal, 0.01);
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                throw new AssertionError("API call failed unexpectedly");
            }
        });


    }

    @Test
    public void testFetchProductFailure() {
        // Simulate API error response
        when(apiMock.getProductByBarcode(testBarcode)).thenReturn(mockCall);

        doAnswer(invocation -> {
            Callback<ProductResponse> callback = invocation.getArgument(0);
            callback.onFailure(mockCall, new Throwable("Network error"));
            return null;
        }).when(mockCall).enqueue(any(Callback.class));

        // Make the API call
        apiMock.getProductByBarcode(testBarcode).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                fail("Expected failure, but got success response");
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                assertEquals("Network error", t.getMessage());
            }
        });
    }
}
