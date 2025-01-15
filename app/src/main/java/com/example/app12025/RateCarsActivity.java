package com.example.app12025;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app12025.data.Database;

public class RateCarsActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_MEDIA_IMAGES = 1001;
    private RecyclerView recyclerView;
    private PostsAdapter postsAdapter;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_cars);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        database = new Database(this);

        // Check and request permission if needed
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQUEST_CODE_READ_MEDIA_IMAGES);
        } else {
            // Permission already granted, proceed with loading posts
            loadPosts();
        }
    }

    // Method to load posts from the database
    private void loadPosts() {
        Cursor cursor = database.getAllPosts();
        postsAdapter = new PostsAdapter(cursor, this);
        recyclerView.setAdapter(postsAdapter);
    }

    // Method to update rating in the database when a user rates a post
    public void updatePostRating(int postId, float rating) {
        // Call the database method to update the rating
        database.updatePostRating(postId, rating);
        Toast.makeText(this, "Rating updated!", Toast.LENGTH_SHORT).show();
    }

    // Handle the permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_MEDIA_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with loading posts
                loadPosts();
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(this, "Permission denied to access media images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}