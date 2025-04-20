package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypetshop.utils.SharedPrefManager;

public class Signup extends AppCompatActivity {

    private SharedPrefManager sharedPrefManager;
    private EditText etName, etEmail, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sharedPrefManager = new SharedPrefManager(this);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Set up the sign-up button listener
        findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (validateInput(name, email, password, confirmPassword)) {
                // Check if user already exists
                if (sharedPrefManager.userExists(email)) {
                    Toast.makeText(Signup.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save user data
                sharedPrefManager.saveUser(email, password, "Ramallah");

                // Show success message
                Toast.makeText(Signup.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                // Navigate to the login screen
                startActivity(new Intent(Signup.this, Login.class));
                finish();
            } else {
                Toast.makeText(Signup.this, "Please make sure all fields are valid", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the login link listener
        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            startActivity(new Intent(Signup.this, Login.class));
        });
    }

    private boolean validateInput(String name, String email, String password, String confirmPassword) {
        return !name.isEmpty() && !email.isEmpty() && !password.isEmpty() && password.equals(confirmPassword);
    }
}
