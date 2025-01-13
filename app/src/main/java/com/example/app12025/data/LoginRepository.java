package com.example.app12025.data;

public class LoginRepository {

    private final LoginDataSource loginDataSource;

    public LoginRepository(LoginDataSource loginDataSource) {
        this.loginDataSource = loginDataSource;
    }

    // Login using the credentials provided
    public boolean login(String username, String password) {
        return loginDataSource.validateCredentials(username, password);  // Check user via database
    }
}