package com.inventory.dao;

import com.inventory.model.AdjustStock;
import com.inventory.model.Product;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdjustStockDAO {

    public List<AdjustStock> getAllAdjustStocks() throws SQLException {
        List<AdjustStock> adjustStocks = new ArrayList<>();
        String sql = "SELECT * FROM AdjustStockView";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                adjustStocks.add(mapResultSetToAdjustStock(rs));
            }
        }
        return adjustStocks;
    }

    public AdjustStock getAdjustStockById(int id) throws SQLException {
        String sql = "SELECT * FROM AdjustStockView WHERE adjustment_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdjustStock(rs);
                }
            }
        }
        return null;
    }

    public void addAdjustStock(AdjustStock adjustStock) throws SQLException {
        String sql = "INSERT INTO adjuststocks (product_id, OPTION, adjust_stock, price, total_price, DATE, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setAdjustStockParameters(ps, adjustStock);
            ps.executeUpdate();
        }
    }

    public void updateAdjustStock(AdjustStock adjustStock) throws SQLException {
        String sql = "UPDATE adjuststocks SET product_id = ?, OPTION = ?, adjust_stock = ?, price = ?, total_price = ?, DATE = ?, description = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setAdjustStockParameters(ps, adjustStock);
            ps.setInt(8, adjustStock.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No adjustment found with ID: " + adjustStock.getId());
            }
        }
    }

    public boolean deleteAdjustStock(int id) throws SQLException {
        String sql = "DELETE FROM adjuststocks WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private AdjustStock mapResultSetToAdjustStock(ResultSet rs) throws SQLException {
        AdjustStock adjustStock = new AdjustStock();
        adjustStock.setId(rs.getInt("adjustment_id"));
        
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("product_name"));
        product.setSku(rs.getString("product_sku"));
        product.setCurrentStock(rs.getInt("product_current_stock"));
        adjustStock.setProduct(product);
        
        adjustStock.setOption(rs.getString("adjustment_type"));
        adjustStock.setAdjustStock(rs.getInt("quantity_adjusted"));
        adjustStock.setPrice(rs.getDouble("unit_price"));
        adjustStock.setTotalPrice(rs.getDouble("total_value"));
        adjustStock.setDate(rs.getString("adjustment_date"));
        adjustStock.setDescription(rs.getString("description"));
        
        return adjustStock;
    }

    private void setAdjustStockParameters(PreparedStatement ps, AdjustStock adjustStock) throws SQLException {
        ps.setInt(1, adjustStock.getProduct().getId());
        ps.setString(2, adjustStock.getOption());
        ps.setInt(3, adjustStock.getAdjustStock());
        ps.setDouble(4, adjustStock.getPrice());
        ps.setDouble(5, adjustStock.getTotalPrice());
        ps.setString(6, adjustStock.getDate());
        ps.setString(7, adjustStock.getDescription());
    }
}