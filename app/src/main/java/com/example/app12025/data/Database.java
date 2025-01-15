package com.example.app12025.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app12025.db";
    private static final int DATABASE_VERSION = 2;  // Update to trigger onUpgrade

    // Users table constants
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Posts table constants
    private static final String TABLE_POSTS = "posts";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMAGE_URI = "image_uri";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_RATING = "rating";  // Rating column
    private static final String COLUMN_ID = "id";  // Unique ID for each post

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table if it doesn't exist
        String createUsersTable = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);

        // Create posts table with a rating column
        String createPostsTable = "CREATE TABLE IF NOT EXISTS " + TABLE_POSTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_IMAGE_URI + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL, " +
                COLUMN_RATING + " REAL DEFAULT 0)";
        db.execSQL(createPostsTable);

        // Add sample users
        addSampleUsers(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            // Add the rating column when upgrading to version 2
            String addRatingColumn = "ALTER TABLE " + TABLE_POSTS + " ADD COLUMN " + COLUMN_RATING + " REAL DEFAULT 0";
            db.execSQL(addRatingColumn);
        }
    }

    // Insert a post into the database
    public void insertPost(String title, String imageUri, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_IMAGE_URI, imageUri);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_RATING, 0);  // Set initial rating as 0

        db.insert(TABLE_POSTS, null, values);
        db.close();
    }

    // Fetch all posts from the database
    public Cursor getAllPosts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_POSTS;
        return db.rawQuery(query, null);
    }

    // Method to check if a user exists in the database with the given username and password
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();

        return exists;
    }

    // Add sample users (admin and user)
    private void addSampleUsers(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "admin123");
        db.insert(TABLE_USERS, null, values);

        values.clear();
        values.put(COLUMN_USERNAME, "user");
        values.put(COLUMN_PASSWORD, "password");
        db.insert(TABLE_USERS, null, values);
    }

    // Update the rating for a post
    public void updatePostRating(int postId, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING, rating);
        db.update(TABLE_POSTS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(postId)});
    }
}