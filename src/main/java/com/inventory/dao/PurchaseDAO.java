package com.inventory.dao;

import com.inventory.model.Purchase;
import com.inventory.model.Supplier;
import com.inventory.model.Product;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseDAO {
    private Connection connection;

    public PurchaseDAO() {
        connection = DBUtil.getConnection();
    }

    // Get all purchases using purchase_details view (read-only)
    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM purchase_details";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Purchase purchase = new Purchase();
                purchase.setId(rs.getInt("purchase_id"));
                Supplier supplier = new Supplier();
                supplier.setId(rs.getInt("suppliers_id"));
                supplier.setName(rs.getString("supplier_name"));
                purchase.setSupplier(supplier);
                Product product = new Product();
                product.setId(rs.getInt("product_id"));
                product.setName(rs.getString("product_name"));
                purchase.setProduct(product);
                purchase.setDate(rs.getDate("purchase_date").toString());
                purchase.setStatus(rs.getString("purchase_status"));
                purchase.setStock(rs.getInt("purchased_stock"));
                purchase.setCost_price(rs.getDouble("purchase_cost_price"));
                purchase.setDescription(rs.getString("purchase_description"));
                purchases.add(purchase);
            }
            System.out.println("Fetched " + purchases.size() + " purchases from purchase_details view");
        } catch (SQLException e) {
            System.err.println("SQL Error in getAllPurchases: " + e.getMessage());
            e.printStackTrace();
        }
        return purchases;
    }

    // Get Purchase by ID using purchase_details view (read-only)
    public Purchase getPurchaseById(int id) {
        Purchase purchase = null;
        String sql = "SELECT * FROM purchase_details WHERE purchase_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    purchase = new Purchase();
                    purchase.setId(rs.getInt("purchase_id"));
                    Supplier supplier = new Supplier();
                    supplier.setId(rs.getInt("suppliers_id"));
                    supplier.setName(rs.getString("supplier_name"));
                    purchase.setSupplier(supplier);
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    purchase.setProduct(product);
                    purchase.setDate(rs.getDate("purchase_date").toString());
                    purchase.setStatus(rs.getString("purchase_status"));
                    purchase.setStock(rs.getInt("purchased_stock"));
                    purchase.setCost_price(rs.getDouble("purchase_cost_price"));
                    purchase.setDescription(rs.getString("purchase_description"));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getPurchaseById: " + e.getMessage());
            e.printStackTrace();
        }
        return purchase;
    }

    // Add Purchase using purchase_write_view (updatable)
    public void addPurchase(Purchase purchase) {
        String sql = "INSERT INTO purchase_write_view (suppliers_id, product_id, purchase_date, purchase_status, purchased_stock, purchase_cost_price, purchase_description) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, purchase.getSupplier().getId());
            ps.setInt(2, purchase.getProduct().getId());
            ps.setDate(3, java.sql.Date.valueOf(purchase.getDate()));
            ps.setString(4, purchase.getStatus());
            ps.setInt(5, purchase.getStock());
            ps.setDouble(6, purchase.getCost_price());
            ps.setString(7, purchase.getDescription());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Purchase added successfully via purchase_write_view");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in addPurchase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update Purchase using purchase_write_view (updatable)
    public void updatePurchase(Purchase purchase) {
        String sql = "UPDATE purchase_write_view SET suppliers_id=?, product_id=?, purchase_date=?, purchase_status=?, purchased_stock=?, purchase_cost_price=?, purchase_description=? WHERE purchase_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, purchase.getSupplier().getId());
            ps.setInt(2, purchase.getProduct().getId());
            ps.setDate(3, java.sql.Date.valueOf(purchase.getDate()));
            ps.setString(4, purchase.getStatus());
            ps.setInt(5, purchase.getStock());
            ps.setDouble(6, purchase.getCost_price());
            ps.setString(7, purchase.getDescription());
            ps.setInt(8, purchase.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Purchase updated successfully via purchase_write_view: ID " + purchase.getId());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in updatePurchase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete Purchase using purchase_write_view (updatable)
    public boolean deletePurchase(int id) {
        String sql = "DELETE FROM purchase_write_view WHERE purchase_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Purchase deleted successfully via purchase_write_view: ID " + id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("SQL Error in deletePurchase: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}