package com.inventory.model;

public class Product {
    private int id;
    private String name;
    private String sku; // Changed to String for flexibility (SKUs may be alphanumeric)
    private Unit unit;
    private Brand brand;
    private Category category;
    private double defaultPrice; // Using camelCase to match Loan's style
    private double sellingPrice;
    private String imageUrl;
    private int currentStock;
    private String descriptions;

    public Product() {
        unit = new Unit();
        brand = new Brand();
        category = new Category();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }
}