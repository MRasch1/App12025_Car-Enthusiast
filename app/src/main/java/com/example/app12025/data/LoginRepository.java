package com.example.app12025.data;

import android.content.Context;

import com.example.app12025.data.Database;
import com.example.app12025.data.Result;
import com.example.app12025.data.model.LoggedInUser;

public class LoginRepository {

    private static LoginRepository instance;
    private Database database;

    // Private constructor to prevent instantiation from outside the class
    private LoginRepository(Context context) {
        database = new Database(context);
    }

    // Public method to get the singleton instance of LoginRepository
    public static LoginRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (LoginRepository.class) {
                if (instance == null) {
                    instance = new LoginRepository(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public Result<LoggedInUser> login(String username, String password) {
        // Your login logic here
        if (database.checkUser(username, password)) {
            LoggedInUser user = new LoggedInUser(username, "Display Name"); // Assuming LoggedInUser class exists
            return new Result.Success<>(user);
        } else {
            return new Result.Error(new Exception("Login failed"));
        }
    }
}