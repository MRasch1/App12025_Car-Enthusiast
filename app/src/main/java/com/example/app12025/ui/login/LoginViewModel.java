package com.example.app12025.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app12025.data.LoginRepository;
import com.example.app12025.R;

public class LoginViewModel extends ViewModel {

    private final LoginRepository loginRepository;
    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    // Validate the username and password
    public void login(String username, String password) {
        // Check credentials via repository
        boolean loginSuccess = loginRepository.login(username, password);

        if (loginSuccess) {
            loginResult.setValue(new LoginResult(new LoggedInUserView(username)));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));  // Login failed
        }
    }

    public void loginDataChanged(String username, String password) {
        if (username.isEmpty()) {
            loginFormState.setValue(new LoginFormState(false, R.string.invalid_username, null));
        } else if (password.isEmpty()) {
            loginFormState.setValue(new LoginFormState(false, null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true, null, null));  // Both are valid
        }
    }
}