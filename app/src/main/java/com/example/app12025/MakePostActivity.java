package com.example.app12025;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.provider.MediaStore;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MakePostActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 103;
    private Uri selectedImageUri;
    private EditText editTextTitle;
    private Button buttonSelectPicture;
    private Button buttonSubmitPost;
    private ImageView selectedImageView;
    private LocationManager locationManager;
    private double latitude, longitude;
    private TextView locationTextView;

    // Launcher for selecting an image
    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Display the selected image in the ImageView
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            selectedImageView.setImageBitmap(bitmap);
                            selectedImageView.setVisibility(android.view.View.VISIBLE);
                        } catch (IOException e) {
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_post);

        editTextTitle = findViewById(R.id.editTextTitle);
        buttonSelectPicture = findViewById(R.id.buttonSelectPicture);
        buttonSubmitPost = findViewById(R.id.buttonSubmitPost);
        selectedImageView = findViewById(R.id.selectedImageView);
        locationTextView = findViewById(R.id.locationTextView);

        // Initialize location manager and listener
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Request location permissions and get location updates
        checkLocationPermissions();

        // Handle image selection
        buttonSelectPicture.setOnClickListener(v -> dispatchSelectPictureIntent());

        // Handle post submission
        buttonSubmitPost.setOnClickListener(v -> submitPost());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove location updates to save resources
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private void dispatchSelectPictureIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.setType("image/*");  // Filter for images only
        selectImageLauncher.launch(pickPhoto);
    }

    private void submitPost() {
        String title = editTextTitle.getText().toString();

        if (selectedImageUri != null && !title.isEmpty() && latitude != 0 && longitude != 0) {
            // Example: You could return the data back to the previous activity or save it
            Intent resultIntent = new Intent();
            resultIntent.putExtra("imageUri", selectedImageUri.toString());
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("latitude", latitude);
            resultIntent.putExtra("longitude", longitude);
            setResult(RESULT_OK, resultIntent);
            finish();  // Finish the activity and return to the previous one
        } else {
            Toast.makeText(this, "Please select an image, enter a title, and ensure GPS is enabled.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if they are not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions are already granted, get the location
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not, request the missing permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

            return;
        }

        // Check if GPS is enabled
        if (!isGpsEnabled()) {
            Toast.makeText(this, "Please enable GPS to get your location", Toast.LENGTH_LONG).show();
            return; // Exit method if GPS is disabled
        }

        // Use both GPS and Network provider for location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }

    // Location listener to receive location updates
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            locationTextView.setText("Location: " + latitude + ", " + longitude);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    // Check if GPS is enabled
    private boolean isGpsEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}