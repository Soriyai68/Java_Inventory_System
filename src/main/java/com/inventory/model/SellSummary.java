package com.inventory.model;

import java.util.Date;

public class SellSummary {
    private String productName;
    private Date date;
    private int totalQuantitySold;
    private double totalRevenue;

    // Default constructor
    public SellSummary() {}

    // Parameterized constructor
    public SellSummary(String productName, Date date, int totalQuantitySold, double totalRevenue) {
        this.productName = productName;
        this.date = date;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalQuantitySold() {
        return totalQuantitySold;
    }

    public void setTotalQuantitySold(int totalQuantitySold) {
        this.totalQuantitySold = totalQuantitySold;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    @Override
    public String toString() {
        return "SellSummary{" +
               "productName='" + productName + '\'' +
               ", date=" + date +
               ", totalQuantitySold=" + totalQuantitySold +
               ", totalRevenue=" + totalRevenue +
               '}';
    }
}
