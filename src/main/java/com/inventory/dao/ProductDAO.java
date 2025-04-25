package com.inventory.dao;

import com.inventory.model.Product;
import com.inventory.model.Unit;
import com.inventory.model.Brand;
import com.inventory.model.Category;
import com.inventory.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection connection;

    public ProductDAO() {
        connection = DBUtil.getConnection();
    }

    // Get all products using ProductView
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM ProductView";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                product.setSku(rs.getString("sku"));
                Unit unit = new Unit();
                unit.setId(rs.getInt("unit_id"));
                unit.setName(rs.getString("unit_name"));
                unit.setSubName(rs.getString("unit_sub_name"));
                unit.setDescription(rs.getString("unit_description"));
                product.setUnit(unit);
                Brand brand = new Brand();
                brand.setId(rs.getInt("brand_id"));
                brand.setName(rs.getString("brand_name"));
                brand.setDescription(rs.getString("brand_description"));
                product.setBrand(brand);
                Category category = new Category();
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));
                category.setCode(rs.getInt("category_code"));
                category.setDescription(rs.getString("category_description"));
                product.setCategory(category);
                product.setDefaultPrice(rs.getDouble("default_price"));
                product.setSellingPrice(rs.getDouble("selling_price"));
                product.setImageUrl(rs.getString("imageUrl"));
                product.setCurrentStock(rs.getInt("current_stock"));
                product.setDescriptions(rs.getString("product_description"));
                products.add(product);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllProducts: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    // Get Product by field (id, name, sku, unit_id, brand_id, category_id) using ProductView where possible
    public List<Product> getFilterProduct(String field, String searchText) {
        List<Product> products = new ArrayList<>();
        String sql;
        switch (field) {
            case "id":
                sql = "SELECT * FROM ProductView WHERE product_id = ?";
                break;
            case "name":
                sql = "SELECT * FROM ProductView WHERE product_name LIKE CONCAT('%', ?, '%')";
                break;
            case "sku":
                sql = "SELECT * FROM ProductView WHERE sku = ?";
                break;
            case "unit_id":
                sql = "SELECT * FROM ProductView WHERE unit_id = ?";
                break;
            case "brand_id":
                sql = "SELECT * FROM ProductView WHERE brand_id = ?";
                break;
            case "category_id":
                sql = "SELECT * FROM ProductView WHERE category_id = ?";
                break;
            default:
                System.err.println("Invalid filter field: " + field);
                return products; // Return empty list for invalid field
        }
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (field.equals("id") || field.equals("unit_id") || field.equals("brand_id") || field.equals("category_id")) {
                ps.setInt(1, Integer.parseInt(searchText));
            } else {
                ps.setString(1, searchText);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setSku(rs.getString("sku"));
                    Unit unit = new Unit();
                    unit.setId(rs.getInt("unit_id"));
                    unit.setName(rs.getString("unit_name"));
                    unit.setSubName(rs.getString("unit_sub_name"));
                    unit.setDescription(rs.getString("unit_description"));
                    product.setUnit(unit);
                    Brand brand = new Brand();
                    brand.setId(rs.getInt("brand_id"));
                    brand.setName(rs.getString("brand_name"));
                    brand.setDescription(rs.getString("brand_description"));
                    product.setBrand(brand);
                    Category category = new Category();
                    category.setId(rs.getInt("category_id"));
                    category.setName(rs.getString("category_name"));
                    category.setCode(rs.getInt("category_code"));
                    category.setDescription(rs.getString("category_description"));
                    product.setCategory(category);
                    product.setDefaultPrice(rs.getDouble("default_price"));
                    product.setSellingPrice(rs.getDouble("selling_price"));
                    product.setImageUrl(rs.getString("imageUrl"));
                    product.setCurrentStock(rs.getInt("current_stock"));
                    product.setDescriptions(rs.getString("product_description"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getFilterProduct: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    // Add Product to Products table
    public void addProduct(Product product) {
        String sql = "INSERT INTO Products (name, sku, unit_id, brand_id, category_id, default_price, selling_price, imageUrl, current_stock, descriptions) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getSku());
            ps.setInt(3, product.getUnit().getId());
            ps.setInt(4, product.getBrand().getId());
            ps.setInt(5, product.getCategory().getId());
            ps.setDouble(6, product.getDefaultPrice());
            ps.setDouble(7, product.getSellingPrice());
            ps.setString(8, product.getImageUrl());
            ps.setInt(9, product.getCurrentStock());
            ps.setString(10, product.getDescriptions());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product added successfully: " + product.getName());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in addProduct: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update Product in Products table
    public void updateProduct(Product product) {
        String sql = "UPDATE Products SET name=?, sku=?, unit_id=?, brand_id=?, category_id=?, default_price=?, selling_price=?, imageUrl=?, current_stock=?, descriptions=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getSku());
            ps.setInt(3, product.getUnit().getId());
            ps.setInt(4, product.getBrand().getId());
            ps.setInt(5, product.getCategory().getId());
            ps.setDouble(6, product.getDefaultPrice());
            ps.setDouble(7, product.getSellingPrice());
            ps.setString(8, product.getImageUrl());
            ps.setInt(9, product.getCurrentStock());
            ps.setString(10, product.getDescriptions());
            ps.setInt(11, product.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully: " + product.getName());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in updateProduct: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete Product from Products table
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM Products WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product deleted successfully: ID " + id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("SQL Error in deleteProduct: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Get Product by ID using ProductView
    public Product getProductById(int id) {
        Product product = null;
        String sql = "SELECT * FROM ProductView WHERE product_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setSku(rs.getString("sku"));
                    Unit unit = new Unit();
                    unit.setId(rs.getInt("unit_id"));
                    unit.setName(rs.getString("unit_name"));
                    unit.setSubName(rs.getString("unit_sub_name"));
                    unit.setDescription(rs.getString("unit_description"));
                    product.setUnit(unit);
                    Brand brand = new Brand();
                    brand.setId(rs.getInt("brand_id"));
                    brand.setName(rs.getString("brand_name"));
                    brand.setDescription(rs.getString("brand_description"));
                    product.setBrand(brand);
                    Category category = new Category();
                    category.setId(rs.getInt("category_id"));
                    category.setName(rs.getString("category_name"));
                    category.setCode(rs.getInt("category_code"));
                    category.setDescription(rs.getString("category_description"));
                    product.setCategory(category);
                    product.setDefaultPrice(rs.getDouble("default_price"));
                    product.setSellingPrice(rs.getDouble("selling_price"));
                    product.setImageUrl(rs.getString("imageUrl"));
                    product.setCurrentStock(rs.getInt("current_stock"));
                    product.setDescriptions(rs.getString("product_description"));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getProductById: " + e.getMessage());
            e.printStackTrace();
        }
        return product;
    }

    // New method: Get Products by Name using ProductView
    public List<Product> getProductsByName(String name) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM ProductView WHERE product_name LIKE CONCAT('%', ?, '%')";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setSku(rs.getString("sku"));
                    Unit unit = new Unit();
                    unit.setId(rs.getInt("unit_id"));
                    unit.setName(rs.getString("unit_name"));
                    unit.setSubName(rs.getString("unit_sub_name"));
                    unit.setDescription(rs.getString("unit_description"));
                    product.setUnit(unit);
                    Brand brand = new Brand();
                    brand.setId(rs.getInt("brand_id"));
                    brand.setName(rs.getString("brand_name"));
                    brand.setDescription(rs.getString("brand_description"));
                    product.setBrand(brand);
                    Category category = new Category();
                    category.setId(rs.getInt("category_id"));
                    category.setName(rs.getString("category_name"));
                    category.setCode(rs.getInt("category_code"));
                    category.setDescription(rs.getString("category_description"));
                    product.setCategory(category);
                    product.setDefaultPrice(rs.getDouble("default_price"));
                    product.setSellingPrice(rs.getDouble("selling_price"));
                    product.setImageUrl(rs.getString("imageUrl"));
                    product.setCurrentStock(rs.getInt("current_stock"));
                    product.setDescriptions(rs.getString("product_description"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getProductsByName: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
}