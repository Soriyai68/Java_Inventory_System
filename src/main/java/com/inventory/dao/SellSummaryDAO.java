package com.inventory.dao;

import com.inventory.model.SellSummary;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SellSummaryDAO {
    
    // Method to retrieve the sell summary data from the database
    public List<SellSummary> getSellSummaryData() throws SQLException {
        List<SellSummary> summaries = new ArrayList<>();
        String sql = "SELECT * FROM sell_summary_view";  // SQL query to fetch data from the view

        // Use try-with-resources for automatic resource management
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            // Iterate through the result set and map each row to a SellSummary object
            while (rs.next()) {
                SellSummary summary = mapResultSetToSummary(rs);
                summaries.add(summary);
            }
        }

        return summaries;
    }

    // Helper method to map the ResultSet row to a SellSummary object
    private SellSummary mapResultSetToSummary(ResultSet rs) throws SQLException {
        SellSummary summary = new SellSummary();
        summary.setProductName(rs.getString("product_name"));  // Map product name
        summary.setDate(rs.getDate("date"));  // Map date of sale
        summary.setTotalQuantitySold(rs.getInt("total_quantity_sold"));  // Map total quantity sold
        summary.setTotalRevenue(rs.getDouble("total_revenue"));  // Map total revenue
        return summary;
    }
}
