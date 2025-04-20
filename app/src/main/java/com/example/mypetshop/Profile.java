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
    private EditText etName,etAddress,etpassword;
    private Button btnSaveChanges,btnLogout;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected int getCurrentNavItem() {
        return R.id.btn_profile;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        setupMenu();
        sharedPrefManager = new SharedPrefManager(this);
        etName=findViewById(R.id.et_name);
        etAddress=findViewById(R.id.et_address);
        btnSaveChanges=findViewById(R.id.btn_save_changes);
        btnLogout=findViewById(R.id.btn_logout);
        String email = sharedPrefManager.getUserEmail();
        String address = sharedPrefManager.getUserAddress();
        etName.setText(email != null ? email : "");
        etAddress.setText(address != null ? address : "");
        btnSaveChanges.setOnClickListener(v->{
            saveChanges();
        });
        btnLogout.setOnClickListener(v-> logout());



    }

    private void logout() {
        sharedPrefManager.logout();
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
    }

    private void saveChanges() {
        String name= etName.getText().toString().trim();
        String address= etAddress.getText().toString().trim();
        if(name.isEmpty()||address.isEmpty()){

            Toast.makeText(this, "Name and Address are Required!", Toast.LENGTH_SHORT).show();

        }else{
            sharedPrefManager.updateUserDetails(name,null,address);
            Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }


}