package com.inventory.model;

public class Category {
    private int id;
    private String name;
    private int code;
    private String description;

    // Default constructor
    public Category() {}

    // Parameterized constructor
    public Category(String name, int code, String description) {
        this.name = name;
        this.code = code;
        this.description = description;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
