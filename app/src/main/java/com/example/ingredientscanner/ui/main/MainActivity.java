package com.example.ingredientscanner.ui.main;

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

import com.example.ingredientscanner.data.local.AppDatabase;
import com.example.ingredientscanner.data.local.ScannedFood;
import com.example.ingredientscanner.data.remote.models.NutritionixAPI;
import com.example.ingredientscanner.data.remote.models.NutritionixResponse;
import com.example.ingredientscanner.data.remote.models.RetrofitClient;
import com.example.ingredientscanner.ui.history.HistoryActivity;
import com.example.ingredientscanner.ui.preferences.NutritionPreferencesActivity;
import com.example.ingredientscanner.R;
import com.example.ingredientscanner.ui.scan.CalorieCheckActivity;
import com.example.ingredientscanner.ui.scan.ScanAnalysisActivity;
import com.example.ingredientscanner.data.remote.models.ProductResponse;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_CODE = 101;

    private Button btnScanBarcode, btnHistory, btnSetKcalLimit, btnTestBarcode, btnCheckCalories;

    private final ActivityResultLauncher<Intent> barcodeLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap barcodeImage = (Bitmap) result.getData().getExtras().get("data");
                    scanBarcode(barcodeImage);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnHistory = findViewById(R.id.btnHistory);
        btnSetKcalLimit = findViewById(R.id.btnSetKcalLimit);
        btnTestBarcode = findViewById(R.id.btnTestBarcode);
        btnCheckCalories = findViewById(R.id.btnCheckCalories);

        btnScanBarcode.setOnClickListener(v -> {
            if (!checkCameraPermission()) {
                requestCameraPermission();
            } else {
                openCameraForBarcode();
            }
        });

        btnTestBarcode.setOnClickListener(v -> {
            // Example default barcode for
            String defaultBarcode = "5449000054227";
            fetchProductByBarcode(defaultBarcode);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnSetKcalLimit.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NutritionPreferencesActivity.class));
        });

        btnCheckCalories.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalorieCheckActivity.class);
            startActivity(intent);
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
        NutritionixAPI api = RetrofitClient.getInstance().create(NutritionixAPI.class);
        Call<NutritionixResponse> call = api.searchProduct(barcode);

        call.enqueue(new Callback<NutritionixResponse>() {
            @Override
            public void onResponse(Call<NutritionixResponse> call, Response<NutritionixResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getFoods().isEmpty()) {
                    NutritionixResponse.Product product = response.body().getFoods().get(0);

                    // Insert scanned product to database
                    ScannedFood food = new ScannedFood(
                            product.name,
                            product.brand,
                            product.ingredients != null ? product.ingredients : "Unknown",
                            product.calories,
                            System.currentTimeMillis()
                    );

                    new Thread(() -> {
                        AppDatabase.getInstance(getApplicationContext())
                                .scannedFoodDao()
                                .insert(food);
                    }).start();

                    navigateToProductDetail(product);
                } else {
                    Toast.makeText(MainActivity.this, "Product not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NutritionixResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch product data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToProductDetail(NutritionixResponse.Product product) {
        Intent kcalIntent = new Intent(MainActivity.this, ScanAnalysisActivity.class);
        kcalIntent.putExtra("productName", product.name);
        kcalIntent.putExtra("brand", product.brand);
        kcalIntent.putExtra("ingredients", product.ingredients != null ? product.ingredients : "Not provided");
        kcalIntent.putExtra("calories", String.valueOf(product.calories));

        startActivity(kcalIntent);
    }
}
