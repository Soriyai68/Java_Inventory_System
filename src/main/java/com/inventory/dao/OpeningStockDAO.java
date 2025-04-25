package com.inventory.dao;

import com.inventory.model.OpeningStock;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OpeningStockDAO {
    private Connection connection;

    public OpeningStockDAO() {
        connection = DBUtil.getConnection();
    }

    /**
     * Helper method to update the current stock of a product.
     * It adds the stockChange (can be positive or negative) to Products.current_stock.
     */
    private void updateProductStock(int productId, int stockChange) {
        String sql = "UPDATE Products SET current_stock = current_stock + ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stockChange);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all opening stock records.
     */
    public List<OpeningStock> getAllOpeningStocks() {
        List<OpeningStock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM OpeningStock";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OpeningStock stock = new OpeningStock();
                stock.setId(rs.getInt("id"));
                stock.setProductId(rs.getInt("product_id"));
                stock.setStock(rs.getInt("stock"));
                stock.setDate(rs.getDate("date"));
                stock.setDescription(rs.getString("description"));
                stocks.add(stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    /**
     * Retrieves opening stock records filtered by a specific field and search text.
     * Supported fields: "id", "product_id", "date", "description".
     */
    public List<OpeningStock> getFilterOpeningStock(String field, String searchText) {
        List<OpeningStock> stocks = new ArrayList<>();
        String sql = "";
        switch (field) {
            case "id":
                sql = "SELECT * FROM OpeningStock WHERE id = ?";
                break;
            case "product_id":
                sql = "SELECT * FROM OpeningStock WHERE product_id = ?";
                break;
            case "date":
                sql = "SELECT * FROM OpeningStock WHERE date = ?";
                break;
            case "description":
                sql = "SELECT * FROM OpeningStock WHERE description LIKE CONCAT('%', ?, '%')";
                break;
            default:
                return stocks;
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if ("id".equals(field) || "product_id".equals(field)) {
                ps.setInt(1, Integer.parseInt(searchText));
            } else {
                ps.setString(1, searchText);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OpeningStock stock = new OpeningStock();
                    stock.setId(rs.getInt("id"));
                    stock.setProductId(rs.getInt("product_id"));
                    stock.setStock(rs.getInt("stock"));
                    stock.setDate(rs.getDate("date"));
                    stock.setDescription(rs.getString("description"));
                    stocks.add(stock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    /**
     * Inserts a new OpeningStock record and updates the associated product's current stock.
     */
    public void addOpeningStock(OpeningStock stock) {
        String sql = "INSERT INTO OpeningStock (product_id, stock, date, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stock.getProductId());
            ps.setInt(2, stock.getStock());
            ps.setDate(3, new java.sql.Date(stock.getDate().getTime()));
            ps.setString(4, stock.getDescription());
            ps.executeUpdate();

            // Update the current stock for the product.
            updateProductStock(stock.getProductId(), stock.getStock());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing OpeningStock record.
     * Also adjusts the product's current stock based on the difference between the old and new stock values.
     */
    public void updateOpeningStock(OpeningStock stock) {
        // Retrieve the old record to calculate the difference in stock
        OpeningStock oldStock = getOpeningStockById(stock.getId());
        int stockDifference = stock.getStock() - oldStock.getStock();

        String sql = "UPDATE OpeningStock SET product_id = ?, stock = ?, date = ?, description = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, stock.getProductId());
            ps.setInt(2, stock.getStock());
            ps.setDate(3, new java.sql.Date(stock.getDate().getTime()));
            ps.setString(4, stock.getDescription());
            ps.setInt(5, stock.getId());
            ps.executeUpdate();

            // Adjust the product's current stock if there is a difference.
            if (stockDifference != 0) {
                updateProductStock(stock.getProductId(), stockDifference);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes an OpeningStock record and subtracts its stock value from the associated product's current stock.
     */
    public void deleteOpeningStock(int id) {
        // Retrieve the stock record to adjust product's current stock later.
        OpeningStock stock = getOpeningStockById(id);
        String sql = "DELETE FROM OpeningStock WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();

            // Subtract the deleted stock value from the product's current stock.
            updateProductStock(stock.getProductId(), -stock.getStock());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a single OpeningStock record by its ID.
     */
    public OpeningStock getOpeningStockById(int id) {
        OpeningStock stock = new OpeningStock();
        String sql = "SELECT * FROM OpeningStock WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stock.setId(rs.getInt("id"));
                    stock.setProductId(rs.getInt("product_id"));
                    stock.setStock(rs.getInt("stock"));
                    stock.setDate(rs.getDate("date"));
                    stock.setDescription(rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }
}
