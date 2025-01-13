package com.example.app12025.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.app12025.data.Database;
import com.example.app12025.data.LoginDataSource;
import com.example.app12025.data.LoginRepository;

public class LoginViewModelFactory implements ViewModelProvider.Factory {

    private final Database database;

    public LoginViewModelFactory(Database database) {
        this.database = database;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        LoginDataSource dataSource = new LoginDataSource(database);
        LoginRepository repository = new LoginRepository(dataSource);
        return (T) new LoginViewModel(repository);
    }
}