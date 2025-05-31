package com.example.ingredientscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 101;

    private Button btnCapture, btnScanBarcode, btnSave, btnHistory, btnSetKcalLimit;
    private ImageView imageView;
    private Bitmap capturedImage;

    private final ActivityResultLauncher<Intent> barcodeLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap barcodeImage = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(barcodeImage);
                    scanBarcode(barcodeImage);
                }
            });

    private final ActivityResultLauncher<Intent> captureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    capturedImage = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(capturedImage);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCapture = findViewById(R.id.btnCapture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnSave = findViewById(R.id.btnSave);

        btnHistory = findViewById(R.id.btnHistory);

        imageView = findViewById(R.id.imageView);

        btnCapture.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCameraForImage();
            } else {
                requestCameraPermission();
            }
        });

        btnScanBarcode.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCameraForBarcode();
            } else {
                requestCameraPermission();
            }
        });

        btnSave.setOnClickListener(v -> saveCapturedImageText());

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnSetKcalLimit = findViewById(R.id.btnSetKcalLimit);

        btnSetKcalLimit.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NutritionPreferencesActivity.class));
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Camera permission granted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openCameraForImage() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureLauncher.launch(cameraIntent);
    }

    private void openCameraForBarcode() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        barcodeLauncher.launch(cameraIntent);
    }

    private void scanBarcode(Bitmap barcodeImage) {
        InputImage image = InputImage.fromBitmap(barcodeImage, 0);
        BarcodeScanner scanner = BarcodeScanning.getClient();

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (!barcodes.isEmpty()) {
                        String barcodeValue = barcodes.get(0).getRawValue();
                        fetchProductByBarcode(barcodeValue);
                    } else {
                        Toast.makeText(this, "No barcode detected.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Barcode scanning failed.", Toast.LENGTH_SHORT).show());
    }

    private void fetchProductByBarcode(String barcode) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenFoodFactAPI api = retrofit.create(OpenFoodFactAPI.class);
        Call<ProductResponse> call = api.getProductByBarcode(barcode);

        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse product = response.body();
                    navigateToProductDetail(product);
                } else {
                    Toast.makeText(MainActivity.this, "Product not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch product data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToProductDetail(ProductResponse product) {
        ScannedFood food = new ScannedFood(
                product.product.product_name,
                product.product.ingredients_text,
                product.product.nutriments != null ? product.product.nutriments.energyKcal : 0,
                System.currentTimeMillis()
        );

        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("productName", product.product.product_name != null ? product.product.product_name : "Unknown");
        intent.putExtra("brand", product.product.brands != null ? product.product.brands : "N/A");
        intent.putExtra("ingredients", product.product.ingredients_text != null ? product.product.ingredients_text : "No ingredients listed");

        // Use a background thread
        new Thread(() -> {
            AppDatabase.getInstance(getApplicationContext()).scannedFoodDao().insert(food);
        }).start();

        // Launch CheckKcalLimitActivity with all product info
        Intent kcalIntent = new Intent(MainActivity.this, ScanAnalysisActivity.class);
        kcalIntent.putExtra("productName", product.product.product_name);
        kcalIntent.putExtra("brand", product.product.brands);
        kcalIntent.putExtra("ingredients", product.product.ingredients_text);
        kcalIntent.putExtra("calories", product.product.nutriments != null ? product.product.nutriments.energyKcal : 0);
        kcalIntent.putExtra("scannedCalories", product.product.nutriments != null ? product.product.nutriments.energyKcal : 0);

        startActivity(kcalIntent);
    }

    private void saveCapturedImageText() {
        if (capturedImage == null) {
            Toast.makeText(this, "No image captured.", Toast.LENGTH_SHORT).show();
            return;
        }

        // You can plug in text extraction here using ML Kit if needed
        File file = new File(getExternalFilesDir(null), "captured_image_info.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Image saved.");  // Placeholder
            Toast.makeText(this, "Saved to " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("SaveFile", "Failed to save file", e);
            Toast.makeText(this, "Error saving file!", Toast.LENGTH_SHORT).show();
        }
    }

    interface OpenFoodFactAPI {
        @GET("api/v0/product/{barcode}.json")
        Call<ProductResponse> getProductByBarcode(@Path("barcode") String barcode);
    }
}
