package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypetshop.utils.SharedPrefManager;

public class Profile extends BaseActivity {
    private SharedPrefManager sharedPrefManager;

    @Override
    protected int getCurrentNavItem() {
        return R.id.btn_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupMenu();

        sharedPrefManager = new SharedPrefManager(this);
        EditText etEmail = findViewById(R.id.et_name);
        EditText etAddress = findViewById(R.id.et_address);

        // Load user data
        etEmail.setText(sharedPrefManager.getUserEmail());
        etAddress.setText(sharedPrefManager.getUserAddress());

        // Save Changes Button
        findViewById(R.id.btn_save_changes).setOnClickListener(v -> {
            String newEmail = etEmail.getText().toString().trim();
            String newAddress = etAddress.getText().toString().trim();

            if (newEmail.isEmpty() || newAddress.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            sharedPrefManager.updateUserDetails(newEmail, null, newAddress);
            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
        });

        // Logout Button
        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            sharedPrefManager.logout();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        });
    }
}