package com.inventory.model;

public class Unit {
    private int id;
    private String name;
    private String subName;
    private String description;

    // Default constructor
    public Unit() {}

    // Parameterized constructor
    public Unit(String name, String subName, String description) {
        this.name = name;
        this.subName = subName;
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

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
