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

import java.util.List;

public class MainActivity extends BaseActivity implements ProductAdapter.OnProductClickListener {
    private RecyclerView rvProducts;
    private TextView tvUsername;
    private ImageView ivUserProfile;
    private SharedPrefManager sharedPrefManager;
    private ProductAdapter productAdapter;
    private ImageButton btnCart; // Add cart button reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupMenu();

        sharedPrefManager = new SharedPrefManager(this);
        initializeViews();
        setupUserInfo();
        setupProducts();
        setupCartButton(); // Initialize cart button

        // Check if the user is logged in
        if (!sharedPrefManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }

    private void initializeViews() {
        rvProducts = findViewById(R.id.rv_products);
        tvUsername = findViewById(R.id.tv_username);
        ivUserProfile = findViewById(R.id.iv_user_profile);
        btnCart = findViewById(R.id.btn_cart); // Initialize cart button from layout
    }

    private void setupProducts() {
        List<Product> products = sharedPrefManager.getProducts();
        productAdapter = new ProductAdapter(products, this);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);
        rvProducts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvProducts.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
    }

    private void setupCartButton() {
        btnCart.setOnClickListener(v -> {
            // Open cart activity
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });

        // You might want to show a badge with cart item count
        updateCartBadge();
    }

    private void updateCartBadge() {
        int cartItemCount = sharedPrefManager.getCartItemCount();
        // Implement your badge logic here if you have one
    }

    @Override
    public void onProductClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    @Override
    public void onAddToCartClick(Product product) {
        // Check if product is in stock
        if (product.getStockQuantity() > 0) {
            // Add to cart without decreasing stock
            sharedPrefManager.addToCart(product, 1);
            Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
            updateCartBadge();
        } else {
            Toast.makeText(this, "This product is out of stock", Toast.LENGTH_SHORT).show();
        }
    }
    private void setupUserInfo() {
        String username = sharedPrefManager.getUserEmail();
        if (username != null && !username.isEmpty()) {
            tvUsername.setText(username);
        }
        // You might want to load user profile image here
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh product list and cart badge when returning from other activities
        productAdapter.updateProducts(sharedPrefManager.getProducts());
        updateCartBadge();
    }

    @Override
    protected int getCurrentNavItem() {
        return R.id.btn_home;
    }
}