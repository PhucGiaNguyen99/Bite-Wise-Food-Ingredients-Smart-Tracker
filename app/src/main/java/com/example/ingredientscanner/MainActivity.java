package com.example.ingredientscanner;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import android.util.Log;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.widget.Toast;

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

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_CODE = 101;
    public static final int BARCODE_REQUEST_CODE = 102;


    private Button btnCapture, btnScanBarcode, btnSave;
    private ImageView imageView;
    private TextView textView;
    private Bitmap capturedImage;   // Store the captured image


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the UI elements
        btnCapture = findViewById(R.id.btnCapture);
        btnScanBarcode = findViewById(R.id.btnScanBarcode);
        btnSave = findViewById(R.id.btnSave);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        // Add the function for the Capture button
        btnCapture.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });

        btnScanBarcode.setOnClickListener(v -> startBarcodeScanner());

        // Add the function for the Save button
        btnSave.setOnClickListener(v -> saveTextToFile());
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
            else {
                Toast.makeText(this, "Camera permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void startBarcodeScanner() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, BARCODE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bitmap barcodeImage = (Bitmap) data.getExtras().get("data");
            scanBarcode(barcodeImage);
        }
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
                        textView.setText("No barcode detected.");
                    }
                })
                .addOnFailureListener(e -> {
                    textView.setText("Barcode scanning failed!");
                });
    }

    private void extractTextFromImage() {
        if (capturedImage == null) {
            textView.setText("No image captured!");
            return;
        }

        InputImage image = InputImage.fromBitmap(capturedImage, 0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(text -> {
                    String extractedText = text.getText();
                    textView.setText(extractedText);    // Display the extracted text in TextView
                })
                .addOnFailureListener(e -> {
                    Log.e("MLKit", "Failed to recognized text", e);
                    Toast.makeText(this, "Text recognition failed!", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveTextToFile() {
        String extractedText = textView.getText().toString();

        if (extractedText.isEmpty() || extractedText.equals("Extracted text will appear here")) {
            textView.setText("No text to save!");
            return;
        }

        File file = new File(getExternalFilesDir(null), "ingredients.txt");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(extractedText);
            Toast.makeText(this, "Text saved to: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            textView.setText("Error saving file!");
            Log.e("FileSave", "Failed to save file", e);
        }
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
                    displayProductInfo(product);
                } else {
                    textView.setText("Product not found in Open Food Facts database.");
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                textView.setText("Failed to fetch product details.");

            }
        });
    }

    private void displayProductInfo(ProductResponse product) {
        StringBuilder info = new StringBuilder();

        info.append("🍽 **Product Name:** ").append(product.product.product_name).append("\n");
        info.append("🏭 **Brand:** ").append(product.product.brands).append("\n\n");

        info.append("✅ **Ingredients:**\n").append(product.product.ingredients_text).append("\n\n");

        if (product.product.nutriments != null) {
            info.append("🔥 **Calories:** ").append(product.product.nutriments.energy_kcal).append(" kcal\n");
        }

        textView.setText(info.toString());
    }

}

interface OpenFoodFactAPI {
    @GET("api/v0/product/{barcode}.json")
    Call<ProductResponse> getProductByBarcode(@Path("barcode") String barcode);
}