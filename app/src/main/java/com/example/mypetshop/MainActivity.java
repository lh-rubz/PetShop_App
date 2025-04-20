package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypetshop.adapters.ProductAdapter;
import com.example.mypetshop.models.Product;
import com.example.mypetshop.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ProductAdapter.OnProductClickListener {
private RecyclerView rvProducts;
private TextView tvUsername;
private ImageView ivUserProfile;
private SharedPrefManager sharedPrefManager;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
setupMenu();
        sharedPrefManager = new SharedPrefManager(this);
initializeViews();
setupUserInfo();
setupProducts();
        // Check if the user is logged in
        if (!sharedPrefManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }

    private void setupProducts() {
        List<Product> products= sharedPrefManager.getProducts();
productAdapter= new ProductAdapter(products,this);
rvProducts.setLayoutManager(new GridLayoutManager(this,2));
rvProducts.setAdapter(productAdapter);
rvProducts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
rvProducts.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));


    }
    @Override
    public void onProductClick(Product product) {
        Toast.makeText(this, "product clicked", Toast.LENGTH_SHORT).show();
        // Handle product click
        // TODO: 4/20/2025 implement the product details page
//        Intent intent = new Intent(this ,this);
//        intent.putExtra("product_id", product.getId());
//        startActivity(intent);
//
    }
    @Override
    public void onAddToCartClick(Product product) {
        // Handle add to cart
        if (product.getStockQuantity() > 0) {
//            sharedPrefManager.addToCart(product);
            product.setStockQuantity(product.getStockQuantity() - 1);
            sharedPrefManager.updateProduct(product);
            productAdapter.notifyDataSetChanged();
            Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "This product is out of stock", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupUserInfo() {
        String username= sharedPrefManager.getUserEmail();
        if(username!=null && !username.isEmpty()){
            tvUsername.setText(username);
        }
    }

    private void initializeViews() {
        rvProducts= findViewById(R.id.rv_products);
        tvUsername=findViewById(R.id.tv_username);
        ivUserProfile= findViewById(R.id.iv_user_profile);
    }


    @Override
    protected int getCurrentNavItem() {
        return R.id.btn_home;
    }
}
