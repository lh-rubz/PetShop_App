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
import com.example.mypetshop.Signup;
import com.example.mypetshop.utils.SharedPrefManager;

public class Login extends AppCompatActivity {
    private EditText edtEmail, edtPassword;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefManager = new SharedPrefManager(this);

        // Check if already logged in
        if (sharedPrefManager.isLoggedIn()) {
            navigateToMainActivity();
            return;
        }

        // Initialize views
        edtEmail = findViewById(R.id.etEmail);
        edtPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        // Login Button Click
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (sharedPrefManager.validateUser(email, password)) {
                sharedPrefManager.setLoggedIn(true);
                navigateToMainActivity();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        // Signup Button
        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            startActivity(new Intent(this, Signup.class));
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}