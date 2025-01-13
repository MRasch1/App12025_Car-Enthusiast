package com.example.app12025.ui.login;

public class LoginFormState {

    private Integer usernameError;
    private Integer passwordError;
    private boolean isDataValid;

    // Constructor that takes a boolean (form validity) and optional errors for username and password
    public LoginFormState(boolean isDataValid, Integer usernameError, Integer passwordError) {
        this.isDataValid = isDataValid;
        this.usernameError = usernameError;
        this.passwordError = passwordError;
    }

    public Integer getUsernameError() {
        return usernameError;
    }

    public Integer getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}