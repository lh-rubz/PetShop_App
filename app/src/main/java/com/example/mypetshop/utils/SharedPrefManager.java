package com.example.mypetshop.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mypetshop.R;
import com.example.mypetshop.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "mypetshop_pref";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
private static final String KEY_PRODUCTS="products";

    private SharedPreferences sharedPreferences;
private Gson gson;
    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
   gson= new Gson();
   initializeSampleProducts(context);
    }

    // Save user login details and address
    public void saveUser(String email, String password, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);
        editor.putString(KEY_ADDRESS, address);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Get saved user email

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }
    public void addProduct(Product product) {
        List<Product> products = getProducts();
        products.add(product);
        saveProducts(products);
    }
    public List<Product> getProducts() {
        String json = sharedPreferences.getString(KEY_PRODUCTS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<Product>>(){}.getType();
        return gson.fromJson(json, type);
    }
    private void saveProducts(List<Product> products) {
        String json = gson.toJson(products);
        sharedPreferences.edit().putString(KEY_PRODUCTS, json).apply();
    }
    // Get saved user password
    public String getUserPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, null);
    }
    public void updateProduct(Product updatedProduct) {
        List<Product> products = getProducts();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(updatedProduct.getId())) {
                products.set(i, updatedProduct);
                break;
            }
        }
        saveProducts(products);
    }
    public void removeProduct(String productId) {
        List<Product> products = getProducts();
        for (Iterator<Product> iterator = products.iterator(); iterator.hasNext();) {
            Product product = iterator.next();
            if (product.getId().equals(productId)) {
                iterator.remove();
                break;
            }
        }
        saveProducts(products);
    }
    public void initializeSampleProducts(Context context) {
        if (getProducts().isEmpty()) {
            List<Product> sampleProducts = new ArrayList<>();

            // Dog Products
            sampleProducts.add(new Product("1", "Premium Dog Food",
                    "High-quality nutrition for your dog", "Dogs", "$24.99",
                    R.drawable.dog_food, 15));
            sampleProducts.add(new Product("2", "Dog Chew Toy",
                    "Durable and safe chew toy for dogs", "Dogs", "$9.99",
                    R.drawable.dog_chew_toy, 20));
            sampleProducts.add(new Product("3", "Dog Bed",
                    "Comfortable bed for your furry friend", "Dogs", "$49.99",
                    R.drawable.dog_bed, 5));

            // Cat Products
            sampleProducts.add(new Product("4", "Cat Litter Box",
                    "Comfortable and easy to clean", "Cats", "$19.99",
                    R.drawable.cat_litter, 8));
            sampleProducts.add(new Product("5", "Cat Scratching Post",
                    "A sturdy scratching post for your cat", "Cats", "$15.99",
                    R.drawable.cat_scratch, 10));
            sampleProducts.add(new Product("6", "Cat Toy Mouse",
                    "Interactive toy for hours of fun", "Cats", "$7.99",
                    R.drawable.cat_toy_mouse, 25));

            // Fish Products
            sampleProducts.add(new Product("7", "Aquarium Filter",
                    "Efficient filter for a clean and healthy aquarium", "Fish", "$29.99",
                    R.drawable.aquarium_filter, 12));
            sampleProducts.add(new Product("8", "Fish Tank Heater",
                    "Maintain the perfect temperature for your fish", "Fish", "$19.99",
                    R.drawable.fish_heater, 6));

            // Bird Products
            sampleProducts.add(new Product("9", "Bird Cage",
                    "Spacious and comfortable cage for your bird", "Birds", "$59.99",
                    R.drawable.bird_cage, 4));
            sampleProducts.add(new Product("10", "Bird Perch",
                    "Perfect perch for your bird to relax", "Birds", "$12.99",
                    R.drawable.bird_perch, 18));

            // Small Animals Products
            sampleProducts.add(new Product("11", "Hamster Cage",
                    "A cozy and secure cage for your hamster", "Small Animals", "$15.99",
                    R.drawable.hamster_cage, 10));

            // Reptile Products
            sampleProducts.add(new Product("13", "Reptile Heat Lamp",
                    "Essential heat source for reptiles", "Reptiles", "$18.99",
                    R.drawable.reptile_heat_lamp, 7));
            sampleProducts.add(new Product("14", "Reptile Habitat",
                    "Perfect environment for your reptile pet", "Reptiles", "$35.99",
                    R.drawable.reptile_habitats, 3));

            // Save the products in shared preferences or database
            saveProducts(sampleProducts);
        }
    }
    public List<Product> filterProductsByCategory(String category) {
        List<Product> allProducts = getProducts();
        if (category.equals("All")) {
            return allProducts;
        }
        List<Product> filtered = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getCategory().equals(category)) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    public Product getProductById(String productId) {
        for (Product product : getProducts()) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null;
    }
    // Get saved user address
    public String getUserAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, null);  // Return saved address
    }

    // Check if the user is logged in
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Log out the user
    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    // Update user details (email, password, and address)
    public void updateUserDetails(String email, String password, String address) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (email != null) {
            editor.putString(KEY_EMAIL, email);
        }
        if (password != null) {
            editor.putString(KEY_PASSWORD, password);
        }
        if (address != null) {
            editor.putString(KEY_ADDRESS, address);
        }
        editor.apply();
    }
}
