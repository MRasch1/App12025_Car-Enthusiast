package com.example.app12025.ui.login;

import androidx.annotation.Nullable;

public class LoginResult {

    @Nullable
    private final LoggedInUserView success;
    @Nullable
    private final Integer error;

    // Constructor for success case
    public LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
        this.error = null;
    }

    // Constructor for failure case, with an error resource ID
    public LoginResult(@Nullable Integer error) {
        this.success = null;
        this.error = error;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}