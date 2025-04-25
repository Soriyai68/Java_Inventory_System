package com.inventory.model;

public class Sell {
    private int id;
    private Product product;
    private Customer customer;
    private int saleStock;
    private double price;
    private double totalPrice;
    private String description;
    private String date;

    // Default constructor initializing Product and Customer objects
    public Sell() {
        product = new Product();
        customer = new Customer();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public int getSaleStock() {
        return saleStock;
    }

    public void setSaleStock(int saleStock) {
        this.saleStock = saleStock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}