package com.example.ingredientscanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.ingredientscanner.data.remote.models.ProductResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RunWith(AndroidJUnit4.class)
public class BarcodeApiTest {
    private MockWebServer mockWebServer;
    private OpenFoodFactAPI api;

    @Before
    public void setup() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/")) // use test server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(OpenFoodFactAPI.class);
    }

    @Test
    public void testFetchProductSuccess() throws Exception {
        String mockJson = "{ \"product\": { \"product_name\": \"Oreo\", \"brands\": \"Nabisco\", \"ingredients_text\": \"Sugar, Flour\", \"nutriments\": { \"energy-kcal\": 150 } } }";

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockJson));

        Response<ProductResponse> response = api.getProductByBarcode("1234567890").execute();

        assertTrue(response.isSuccessful());
        assertNotNull(response.body());
        assertEquals("Oreo", response.body().product.product_name);
    }

    @After
    public void teardown() throws Exception {
        mockWebServer.shutdown();
    }
}
