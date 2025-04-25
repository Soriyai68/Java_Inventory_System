package com.inventory.model;

public class AdjustStock {
    private int id;
    private Product product;
    private String option;
    private int adjustStock;
    private double price;
    private double totalPrice;
    private String date;
    private String description;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public String getOption() { return option; }
    public void setOption(String option) { this.option = option; }

    public int getAdjustStock() { return adjustStock; }
    public void setAdjustStock(int adjustStock) { this.adjustStock = adjustStock; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}