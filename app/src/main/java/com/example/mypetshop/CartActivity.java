package com.example.mypetshop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypetshop.adapters.CartAdapter;
import com.example.mypetshop.models.CartItem;
import com.example.mypetshop.models.Product;
import com.example.mypetshop.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends BaseActivity {
    private RecyclerView rvCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
       setupMenu();
       sharedPrefManager = new SharedPrefManager(this);
       initializeViews();
       setupCart();

    }
    private void setupCart() {
        List<CartItem> cartItems = sharedPrefManager.getCartItems();

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        CartAdapter adapter = new CartAdapter(cartItems, new CartAdapter.CartListener() {
            @Override
            public void onQuantityChanged(String productId, int newQuantity) {
                sharedPrefManager.updateCartItemQuantity(productId, newQuantity);
                updateTotal();
            }

            @Override
            public void onItemRemoved(String productId) {
                sharedPrefManager.removeFromCart(productId);
                setupCart(); // Refresh cart
            }
        }, sharedPrefManager);

        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(adapter);

        updateTotal();

        btnCheckout.setOnClickListener(v -> {
            if (sharedPrefManager.checkout()) {
                Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Some items are no longer available", Toast.LENGTH_SHORT).show();
                setupCart(); // Refresh the cart view
            }
        });  }

    private void updateTotal() {
        double total = 0.0;
        List<CartItem> cartItems = sharedPrefManager.getCartItems();

        for (CartItem item : cartItems) {
            if (sharedPrefManager.isProductAvailable(item.getProduct().getId(), item.getQuantity())) {
                String priceStr = item.getProduct().getPrice().replace("$", "");
                double price = Double.parseDouble(priceStr);
                total += price * item.getQuantity();
            }
        }

        tvTotalPrice.setText(String.format("Total: $%.2f", total));
    }
    private void initializeViews() {
        rvCartItems = findViewById(R.id.rv_cart_items);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        btnCheckout = findViewById(R.id.btn_checkout);
    }
    @Override
    protected int getCurrentNavItem() {
        return R.id.btn_cart;
    }
}