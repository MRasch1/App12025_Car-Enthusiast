package com.example.app12025;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.app12025.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1002; // Unique request code for storage permission

    private Uri imageUri;

    // Declare the launcher for taking picture
    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // Debugging log for result handling
                if (result.getResultCode() == RESULT_OK) {
                    Log.d("CameraTest", "Picture captured successfully");
                    // Handle image after capture
                    Toast.makeText(MainActivity.this, "Picture saved: " + imageUri.toString(), Toast.LENGTH_SHORT).show();

                    // Optional: Trigger media scan to make the image appear in gallery
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, imageUri));
                } else {
                    Log.e("CameraTest", "Picture capture failed or cancelled");
                    Toast.makeText(MainActivity.this, "Picture capture failed or cancelled", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Logout button
        Button buttonLogout = findViewById(R.id.button1);
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Take Picture button
        Button buttonTakePicture = findViewById(R.id.button2);
        buttonTakePicture.setOnClickListener(v -> checkPermissionsAndTakePicture());

        // Rate Cars button
        Button buttonRateCars = findViewById(R.id.button4);
        buttonRateCars.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Rate Cars clicked", Toast.LENGTH_SHORT).show();
        });

        // Make Post button
        Button buttonMakePost = findViewById(R.id.button3);
        buttonMakePost.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Make Post clicked", Toast.LENGTH_SHORT).show();
        });
    }

    // Check for camera and storage permission before dispatching the camera intent
    private void checkPermissionsAndTakePicture() {
        // Check for camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
        // For Android 9 and below, check for storage permission
        else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            // Proceed to take the picture
            dispatchTakePictureIntent();
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                checkPermissionsAndTakePicture();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Debugging log to see if camera intent resolves
        Log.d("CameraTest", "dispatchTakePictureIntent: Resolving camera intent");

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Log.d("CameraTest", "Camera intent resolved");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "IMG_" + System.currentTimeMillis() + ".jpg");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            // Check if URI is being created
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (imageUri != null) {
                Log.d("CameraTest", "Image URI created successfully: " + imageUri.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                takePictureLauncher.launch(takePictureIntent);
            } else {
                Log.e("CameraTest", "Error creating image URI");
                Toast.makeText(this, "Error creating file for image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("CameraTest", "No activity found to handle camera intent");
            Toast.makeText(this, "No camera app found to capture image", Toast.LENGTH_SHORT).show();
        }
    }
}