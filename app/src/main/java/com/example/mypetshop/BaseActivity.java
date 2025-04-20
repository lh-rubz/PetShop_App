package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getCurrentNavItem(); // Each activity will implement this

    protected void setupMenu() {
        ImageButton btnHome = findViewById(R.id.btn_home);
        ImageButton btnSearch = findViewById(R.id.btn_search);
        ImageButton btnCart = findViewById(R.id.btn_cart);
        ImageButton btnProfile = findViewById(R.id.btn_profile);



        btnHome.setOnClickListener(v -> {
            if (getCurrentNavItem() != R.id.btn_home) {
                startActivity(new Intent(this, MainActivity.class));
            }
        });

        btnSearch.setOnClickListener(v -> {
            if (getCurrentNavItem() != R.id.btn_search) {
                startActivity(new Intent(this, Search.class));
            }
        });

        btnCart.setOnClickListener(v -> {
            if (getCurrentNavItem() != R.id.btn_cart) {
                startActivity(new Intent(this, Cart.class));
            }
        });

        btnProfile.setOnClickListener(v -> {
            if (getCurrentNavItem() != R.id.btn_profile) {
                startActivity(new Intent(this, Profile.class));
            }
        });
    }
}