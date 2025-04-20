package com.example.mypetshop.models;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private String category;
    private String price;
    private int imageRes;
    private int stockQuantity;

    public Product(String id, String name, String description, String category,
                   String price, int imageRes, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.imageRes = imageRes;
        this.stockQuantity = stockQuantity;
    }   public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    // Getters and Setters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public int getImageRes() { return imageRes; }
    public int getStockQuantity() { return stockQuantity; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(String price) { this.price = price; }
    public void setImageRes(int imageRes) { this.imageRes = imageRes; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
}