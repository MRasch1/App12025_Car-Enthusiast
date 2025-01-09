package com.example.app12025.ui.login;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private Application application;

    public LoginViewModelFactory(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        // Now the ViewModel constructor accepts Application
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(application); // Pass Application here
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}