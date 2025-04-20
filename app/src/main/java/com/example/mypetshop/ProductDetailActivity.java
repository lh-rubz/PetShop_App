package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mypetshop.models.Product;
import com.example.mypetshop.utils.SharedPrefManager;

public class ProductDetailActivity extends AppCompatActivity {
private ImageView ivProductImage;
private TextView tvProductName, tvProductDescription,tvProductPrice, tvProductCategory,tvStockQuantity,tvQuantity;
private Button btnAddToCart, btnBack,btnIncrease,btnDecrease;
private SharedPrefManager sharedPrefManager;
private Product currentProduct;
    private int quantity = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
sharedPrefManager= new SharedPrefManager(this);
String productId= getIntent().getStringExtra("product_id");
currentProduct= sharedPrefManager.getProductById(productId);
initializeViews();
populateProductData();
setupClickListeners();

    }

    private void setupClickListeners() {
        // Increase quantity
        btnIncrease.setOnClickListener(v -> {
            if (quantity < currentProduct.getStockQuantity()) {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            } else {
                Toast.makeText(this, "Cannot exceed available stock", Toast.LENGTH_SHORT).show();
            }
        });

        // Decrease quantity
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Add to cart
        btnAddToCart.setOnClickListener(v -> {
            if (currentProduct.getStockQuantity() > 0) {
                // Update stock

                sharedPrefManager.updateProduct(currentProduct);

                sharedPrefManager.addToCart(currentProduct, quantity);

                // Show success message
                Toast.makeText(this,
                        "Added " + quantity + " " + currentProduct.getName() + " to cart",
                        Toast.LENGTH_SHORT).show();

                // Update UI
                populateProductData();
            }
        });

        // Back button
        btnBack.setOnClickListener(v ->
                {startActivity(new Intent(this, MainActivity.class));}
        );
    }

    private void initializeViews() {
        ivProductImage = findViewById(R.id.iv_product_image);
        tvProductName = findViewById(R.id.tv_product_name);
        tvProductDescription = findViewById(R.id.tv_product_description);
        tvProductPrice = findViewById(R.id.tv_product_price);
        tvProductCategory = findViewById(R.id.tv_product_category);
        tvStockQuantity = findViewById(R.id.tv_stock_quantity);
        tvQuantity = findViewById(R.id.tv_quantity);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnBack = findViewById(R.id.btn_back);
        btnIncrease = findViewById(R.id.btn_increase);
        btnDecrease = findViewById(R.id.btn_decrease);
    }
    private void populateProductData() {
        if (currentProduct != null) {
            ivProductImage.setImageResource(currentProduct.getImageRes());
            tvProductName.setText(currentProduct.getName());
            tvProductDescription.setText(currentProduct.getDescription());
            tvProductPrice.setText(currentProduct.getPrice());
            tvProductCategory.setText("Category: " + currentProduct.getCategory());

            // Handle stock display
            if (currentProduct.getStockQuantity() > 0) {
                tvStockQuantity.setText("In Stock: " + currentProduct.getStockQuantity());
                tvStockQuantity.setTextColor(getResources().getColor(R.color.accent));
                btnAddToCart.setEnabled(true);
                btnAddToCart.setAlpha(1f);
            } else {
                tvStockQuantity.setText("Out of Stock");
                tvStockQuantity.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                btnAddToCart.setEnabled(false);
                btnAddToCart.setAlpha(0.5f);
            }

            // Reset quantity to 1 when product changes
            quantity = 1;
            tvQuantity.setText(String.valueOf(quantity));
        }

    }



}