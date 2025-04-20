package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mypetshop.utils.SharedPrefManager;

public class MainActivity extends BaseActivity {

    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
setupMenu();
        sharedPrefManager = new SharedPrefManager(this);

        // Check if the user is logged in
        if (!sharedPrefManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }


    @Override
    protected int getCurrentNavItem() {
        return R.id.btn_home;
    }
}
