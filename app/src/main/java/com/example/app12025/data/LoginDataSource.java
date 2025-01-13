package com.example.app12025.data;

public class LoginDataSource {

    private final Database database;

    public LoginDataSource(Database database) {
        this.database = database;
    }

    // Use the checkUser method to validate the login
    public boolean validateCredentials(String username, String password) {
        return database.checkUser(username, password);
    }
}