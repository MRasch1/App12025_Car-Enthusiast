package com.example.app12025.ui.login;

public class LoginResult {

    private boolean isSuccess;
    private String errorMessage;

    // Constructor for success case
    public LoginResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    // Constructor for failure case, with an error message
    public LoginResult(boolean isSuccess, String errorMessage) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}