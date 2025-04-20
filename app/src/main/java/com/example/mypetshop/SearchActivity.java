package com.example.mypetshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypetshop.adapters.ProductAdapter;
import com.example.mypetshop.models.Product;
import com.example.mypetshop.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchActivity extends BaseActivity implements ProductAdapter.OnProductClickListener {
    private EditText searchBar;
    private RecyclerView rvProducts;
    private LinearLayout filterContainer;
    private ProductAdapter productAdapter;
    private SharedPrefManager sharedPrefManager;
    private Set<String> selectedCategories = new HashSet<>();
    @Override
    protected int getCurrentNavItem() {
        return R.id.btn_search;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        setupMenu();
        sharedPrefManager = new SharedPrefManager(this);
        initializeViews();
        setupSearch();
        setupFilters();
        setupProducts();


    }
    private void initializeViews() {
        searchBar = findViewById(R.id.search_bar);
        rvProducts = findViewById(R.id.rv_products);
        filterContainer = findViewById(R.id.filter_container);
    }
    private void setupSearch() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString(), selectedCategories);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void setupFilters() {
        // Clear any existing views
        filterContainer.removeAllViews();

        // Define your categories
        List<String> categories = new ArrayList<>();
        categories.add("All");
        categories.add("Dogs");
        categories.add("Cats");
        categories.add("Fish");
        categories.add("Birds");
        categories.add("Small Animals");
        categories.add("Reptiles");

        // Create filter chips programmatically
        for (String category : categories) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(category);
            checkBox.setTextColor(getResources().getColor(R.color.text_primary));
            checkBox.setButtonTintList(getResources().getColorStateList(R.color.primary));

            // Set layout params for spacing between chips
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 16, 0);
            checkBox.setLayoutParams(params);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (category.equals("All")) {
                        // If "All" is selected, clear other selections
                        selectedCategories.clear();
                        selectedCategories.add("All");
                        // Uncheck other checkboxes
                        for (int i = 0; i < filterContainer.getChildCount(); i++) {
                            View child = filterContainer.getChildAt(i);
                            if (child instanceof CheckBox && !((CheckBox) child).getText().equals("All")) {
                                ((CheckBox) child).setChecked(false);
                            }
                        }
                    } else {
                        // Remove "All" if another category is selected
                        selectedCategories.remove("All");
                        selectedCategories.add(category);
                        // Uncheck "All" if it was selected
                        for (int i = 0; i < filterContainer.getChildCount(); i++) {
                            View child = filterContainer.getChildAt(i);
                            if (child instanceof CheckBox && ((CheckBox) child).getText().equals("All")) {
                                ((CheckBox) child).setChecked(false);
                            }
                        }
                    }
                } else {
                    selectedCategories.remove(category);
                }

                filterProducts(searchBar.getText().toString(), selectedCategories);
            });

            filterContainer.addView(checkBox);
        }
    }
    private void setupProducts() {
        List<Product> allProducts = sharedPrefManager.getProducts();
        productAdapter = new ProductAdapter(allProducts, this);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(productAdapter);
    }
    private void filterProducts(String query, Set<String> categories) {
        List<Product> filtered = new ArrayList<>();
        List<Product> allProducts = sharedPrefManager.getProducts();

        for (Product product : allProducts) {
            boolean matchesSearch = query.isEmpty() ||
                    product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getDescription().toLowerCase().contains(query.toLowerCase());

            boolean matchesCategory = categories.isEmpty() ||
                    categories.contains("All") ||
                    categories.contains(product.getCategory());

            if (matchesSearch && matchesCategory) {
                filtered.add(product);
            }
        }

        productAdapter.updateProducts(filtered);
    }
    @Override
    public void onProductClick(Product product) {
        // Handle product click (open detail activity)
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra("product_id", product.getId());
        startActivity(intent);
    }

    @Override
    public void onAddToCartClick(Product product) {
        // Handle add to cart
        if (product.getStockQuantity() > 0) {
            sharedPrefManager.addToCart(product, 1);
            Toast.makeText(this, product.getName() + " added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "This product is out of stock", Toast.LENGTH_SHORT).show();
        }
    }





}