package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypetshop.MainActivity;
import com.example.mypetshop.R;
import com.example.mypetshop.utils.SharedPrefManager;

public class Login extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize SharedPrefManager
        sharedPrefManager = new SharedPrefManager(this);

        // UI elements
        edtEmail = findViewById(R.id.etEmail);
        edtPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Check if already logged in
        if (sharedPrefManager.isLoggedIn()) {
            // User is already logged in, so go directly to the main activity
            navigateToMainActivity();
        }

        // Login Button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                // Validate user input
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Please fill in both fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Assuming successful login, save the data
                sharedPrefManager.saveUser(email, password,"Ramallah");

                // Show success message
                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Navigate to Main Activity after successful login
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();  // Close the login activity to prevent user from going back to login screen
    }
}
