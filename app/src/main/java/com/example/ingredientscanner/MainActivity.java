package com.example.ingredientscanner;

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

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int CAMERA_PERMISSION_CODE = 102;


    private Button btnCapture, btnSave;
    private ImageView imageView;
    private TextView textView;
    private Bitmap capturedImage;   // Store the captured image


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link the UI elements
        btnCapture = findViewById(R.id.btnCapture);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                capturedImage = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(capturedImage);
                extractTextFromImage(); // Process the image after capturing
            } else {
                Toast.makeText(this, "Error capturing image!", Toast.LENGTH_SHORT).show();
            }
        }
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
}