package com.inventory.dao;

import com.inventory.model.Category;
import com.inventory.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {
    private Connection connection;

    public CategoryDAO() {
        connection = DBUtil.getConnection();
    }

    // Get all categories
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Categories");
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setCode(rs.getInt("code"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Get Category by field (id, name, code, or description)
    public List<Category> getFilterCategory(String field, String searchText) {
        List<Category> categories = new ArrayList<>();
        try {
            String sql = "";
            switch (field) {
                case "id":
                    sql = "SELECT * FROM Categories WHERE id=?";
                    break;
                case "name":
                    sql = "SELECT * FROM Categories WHERE name LIKE CONCAT('%', ?, '%')";
                    break;
                case "code":
                    sql = "SELECT * FROM Categories WHERE code=?";
                    break;
                case "description":
                    sql = "SELECT * FROM Categories WHERE description LIKE CONCAT('%', ?, '%')";
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            if (field.equals("id") || field.equals("code")) {
                ps.setInt(1, Integer.parseInt(searchText));
            } else {
                ps.setString(1, searchText);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setCode(rs.getInt("code"));
                category.setDescription(rs.getString("description"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Add Category
    public void addCategory(Category category) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Categories (name, code, description) VALUES (?, ?, ?)"
            );
            ps.setString(1, category.getName());
            ps.setInt(2, category.getCode());
            ps.setString(3, category.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Category
    public void updateCategory(Category category) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "UPDATE Categories SET name=?, code=?, description=? WHERE id=?"
            );
            ps.setString(1, category.getName());
            ps.setInt(2, category.getCode());
            ps.setString(3, category.getDescription());
            ps.setInt(4, category.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Category
    public void deleteCategory(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM Categories WHERE id=?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Category by ID
    public Category getCategoryById(int id) {
        Category category = new Category();
        try {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Categories WHERE id=?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));
                category.setCode(rs.getInt("code"));
                category.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
}
