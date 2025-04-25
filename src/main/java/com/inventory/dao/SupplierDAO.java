package com.inventory.dao;

import com.inventory.model.Supplier;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private Connection connection;

    public SupplierDAO() {
        connection = DBUtil.getConnection();
    }

    /**
     * Retrieves all supplier records.
     */
    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("id"));
                supplier.setName(rs.getString("name"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setEmail(rs.getString("email"));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Retrieves supplier records filtered by a specific field and search text.
     * Supported fields: "id", "name", "phone", "email".
     */
    public List<Supplier> getFilterSuppliers(String field, String searchText) {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "";
        switch (field) {
            case "id":
                sql = "SELECT * FROM Suppliers WHERE id = ?";
                break;
            case "name":
                sql = "SELECT * FROM Suppliers WHERE name LIKE CONCAT('%', ?, '%')";
                break;
            case "phone":
                sql = "SELECT * FROM Suppliers WHERE phone = ?";
                break;
            case "email":
                sql = "SELECT * FROM Suppliers WHERE email LIKE CONCAT('%', ?, '%')";
                break;
            default:
                return suppliers;
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if ("id".equals(field) || "phone".equals(field)) {
                ps.setInt(1, Integer.parseInt(searchText));
            } else {
                ps.setString(1, searchText);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Supplier supplier = new Supplier();
                    supplier.setId(rs.getInt("id"));
                    supplier.setName(rs.getString("name"));
                    supplier.setPhone(rs.getString("phone"));
                    supplier.setEmail(rs.getString("email"));
                    suppliers.add(supplier);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Inserts a new Supplier record.
     */
    public void addSupplier(Supplier supplier) {
        String sql = "INSERT INTO Suppliers (name, phone, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getPhone());
            ps.setString(3, supplier.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing Supplier record.
     */
    public void updateSupplier(Supplier supplier) {
        String sql = "UPDATE Suppliers SET name = ?, phone = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, supplier.getName());
            ps.setString(2, supplier.getPhone());
            ps.setString(3, supplier.getEmail());
            ps.setInt(4, supplier.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Supplier record.
     */
    public void deleteSupplier(int id) {
        String sql = "DELETE FROM Suppliers WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a single Supplier record by its ID.
     */
    public Supplier getSupplierById(int id) {
        Supplier supplier = new Supplier();
        String sql = "SELECT * FROM Suppliers WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    supplier.setId(rs.getInt("id"));
                    supplier.setName(rs.getString("name"));
                    supplier.setPhone(rs.getString("phone"));
                    supplier.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }
}
