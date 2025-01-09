package com.example.app12025.ui.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.app12025.R;
import com.example.app12025.data.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LoginRepository loginRepository;

    // Constructor that takes Application context
    public LoginViewModel(Application application) {
        super(application);
        loginRepository = LoginRepository.getInstance(application);
    }

    // Validate username and password
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true)); // Valid data
        }
    }

    // Check if the username is non-empty
    private boolean isUserNameValid(String username) {
        // The username should not be empty
        return username != null && !username.trim().isEmpty();
    }

    // Check if the password is at least 6 characters long
    private boolean isPasswordValid(String password) {
        return password != null && password.length() > 5;
    }

    // Perform the actual login operation
    public void login(String username, String password) {
        // Here you would use your repository to authenticate the user
        if (isUserNameValid(username) && isPasswordValid(password)) {
            // Simulate a successful login
            loginResult.setValue(new LoginResult(true)); // Success
        } else {
            // If login fails
            loginResult.setValue(new LoginResult(false, "Invalid credentials")); // Failure
        }
    }

    // Getter for LoginResult
    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    // Getter for LoginFormState
    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
}