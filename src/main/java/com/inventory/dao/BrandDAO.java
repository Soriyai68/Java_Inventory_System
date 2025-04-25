package com.inventory.dao;

import com.inventory.model.Brand;
import com.inventory.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {
    private Connection connection;

    public BrandDAO() {
        connection = DBUtil.getConnection();
    }

    public List<Brand> getAllBrands() {
        List<Brand> brands = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Brands");
            while (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getInt("id"));
                brand.setName(rs.getString("name"));
                brand.setDescription(rs.getString("description"));
                brands.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    public List<Brand> getFilterBrand(String field, String searchText) {
        List<Brand> brands = new ArrayList<>();
        try {
            String sql = "";
            switch (field) {
                case "id":
                    sql = "SELECT * FROM Brands WHERE id=?";
                    break;
                case "name":
                    sql = "SELECT * FROM Brands WHERE name LIKE CONCAT('%', ?, '%')";
                    break;
                case "description":
                    sql = "SELECT * FROM Brands WHERE description LIKE CONCAT('%', ?, '%')";
                    break;
            }
            PreparedStatement ps = connection.prepareStatement(sql);
            if (field.equals("id")) {
                ps.setInt(1, Integer.parseInt(searchText));
            } else {
                ps.setString(1, searchText);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Brand brand = new Brand();
                brand.setId(rs.getInt("id"));
                brand.setName(rs.getString("name"));
                brand.setDescription(rs.getString("description"));
                brands.add(brand);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brands;
    }

    public void addBrand(Brand brand) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Brands (name, description) VALUES (?, ?)"
            );
            ps.setString(1, brand.getName());
            ps.setString(2, brand.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBrand(Brand brand) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "UPDATE Brands SET name=?, description=? WHERE id=?"
            );
            ps.setString(1, brand.getName());
            ps.setString(2, brand.getDescription());
            ps.setInt(3, brand.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBrand(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM Brands WHERE id=?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Brand getBrandById(int id) {
        Brand brand = new Brand();
        try {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Brands WHERE id=?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                brand.setId(rs.getInt("id"));
                brand.setName(rs.getString("name"));
                brand.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return brand;
    }
}
