package com.inventory.model;

import java.util.Date;

public class OpeningStock {
    private int id;
    private int productId;
    private int stock;
    private Date date;
    private String description;

    // Default constructor
    public OpeningStock() {}

    // Parameterized constructor
    public OpeningStock(int productId, int stock, Date date, String description) {
        this.productId = productId;
        this.stock = stock;
        this.date = date;
        this.description = description;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
