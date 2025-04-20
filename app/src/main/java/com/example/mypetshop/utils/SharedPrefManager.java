package com.example.mypetshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "mypetshop_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private SharedPreferences sharedPreferences;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save user login details and address
    public void saveUser(String email, String password, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_ADDRESS, address);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Get saved user email
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    // Get saved user password
    public String getUserPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }

    // Get saved user address
    public String getUserAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, null);  // Return saved address
    }

    // Check if the user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Log out the user
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Update user details (email, password, and address)
    public void updateUserDetails(String email, String password, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (email != null) {
            editor.putString(KEY_EMAIL, email);
        }
        if (password != null) {
            editor.putString(KEY_PASSWORD, password);
        }
        if (address != null) {
            editor.putString(KEY_ADDRESS, address);
        }
        editor.apply();
    }
}
