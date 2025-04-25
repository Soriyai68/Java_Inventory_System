package com.inventory.dao;

import com.inventory.model.Sell;
import com.inventory.model.Customer;
import com.inventory.model.Product;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellDAO {
    public List<Sell> getAllSells() throws SQLException {
        List<Sell> sells = new ArrayList<>();
        String sql = "SELECT s.*, p.name AS product_name, c.name AS customer_name " +
                     "FROM Sells s " +
                     "JOIN Products p ON s.product_id = p.id " +
                     "JOIN Customers c ON s.customer_id = c.id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Sell sell = mapResultSetToSell(rs);
                sells.add(sell);
            }
        }
        return sells;
    }

    public Sell getSellById(int id) throws SQLException {
        String sql = "SELECT s.*, p.name AS product_name, c.name AS customer_name " +
                     "FROM Sells s " +
                     "JOIN Products p ON s.product_id = p.id " +
                     "JOIN Customers c ON s.customer_id = c.id " +
                     "WHERE s.id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSell(rs);
                }
            }
        }
        return null;
    }

    public void addSell(Sell sell) throws SQLException {
        String sql = "INSERT INTO Sells (product_id, customer_id, sale_stock, description, date, price, total_price) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setSellParameters(ps, sell);
            ps.executeUpdate();
        }
    }

    public void updateSell(Sell sell) throws SQLException {
        String sql = "UPDATE Sells SET product_id=?, customer_id=?, sale_stock=?, description=?, date=?, price=?, total_price=? WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setSellParameters(ps, sell);
            ps.setInt(8, sell.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("No sale found with ID: " + sell.getId());
        }
    }

    public boolean deleteSell(int id) throws SQLException {
        String sql = "DELETE FROM Sells WHERE id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Sell mapResultSetToSell(ResultSet rs) throws SQLException {
        Sell sell = new Sell();
        sell.setId(rs.getInt("id"));
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("product_name"));
        sell.setProduct(product);
        Customer customer = new Customer();
        customer.setId(rs.getInt("customer_id"));
        customer.setName(rs.getString("customer_name"));
        sell.setCustomer(customer);
        sell.setDate(rs.getDate("date").toString());
        sell.setSaleStock(rs.getInt("sale_stock"));
        sell.setPrice(rs.getDouble("price"));
        sell.setTotalPrice(rs.getDouble("total_price"));
        sell.setDescription(rs.getString("description"));
        return sell;
    }

    private void setSellParameters(PreparedStatement ps, Sell sell) throws SQLException {
        ps.setInt(1, sell.getProduct().getId());
        ps.setInt(2, sell.getCustomer().getId());
        ps.setInt(3, sell.getSaleStock());
        ps.setString(4, sell.getDescription());
        ps.setDate(5, Date.valueOf(sell.getDate()));
        ps.setDouble(6, sell.getPrice());
        ps.setDouble(7, sell.getTotalPrice());
    }
}