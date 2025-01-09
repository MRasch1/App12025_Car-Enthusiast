package com.example.app12025.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app12025.R;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private ProgressBar loading;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        loading = findViewById(R.id.loading);

        // Use ViewModelProvider to create the ViewModel with the factory
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory(getApplication())).get(LoginViewModel.class);

        // Observe the LoginFormState LiveData from ViewModel
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) return;

            // Enable or disable the login button based on form validity
            loginButton.setEnabled(loginFormState.isDataValid());

            // Show errors if username or password is invalid
            if (loginFormState.getUsernameError() != null) {
                username.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                password.setError(getString(loginFormState.getPasswordError()));
            }
        });

        // Set listeners for username and password input to trigger validation
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                loginViewModel.loginDataChanged(username.getText().toString(), password.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
                loginViewModel.loginDataChanged(username.getText().toString(), password.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Handle login button click
        loginButton.setOnClickListener(v -> {
            // Show loading spinner
            loading.setVisibility(View.VISIBLE);

            // Call the login method from ViewModel
            String usernameInput = username.getText().toString();
            String passwordInput = password.getText().toString();
            loginViewModel.login(usernameInput, passwordInput);
        });

        // Observe the login result to show the appropriate message or navigate
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) return;

            // Hide loading spinner
            loading.setVisibility(View.GONE);

            if (loginResult.isSuccess()) {
                // Handle successful login
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                // Navigate to the next activity
            } else {
                // Handle failed login
                Toast.makeText(LoginActivity.this, loginResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}