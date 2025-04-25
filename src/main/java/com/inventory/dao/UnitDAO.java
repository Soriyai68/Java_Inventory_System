package com.inventory.dao;

import com.inventory.model.Unit;
import com.inventory.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDAO {
    private Connection connection;

    public UnitDAO() {
        connection = DBUtil.getConnection();
    }

    // Get all units
    public List<Unit> getAllUnits() {
        List<Unit> units = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Units");
            while (rs.next()) {
                Unit unit = new Unit();
                unit.setId(rs.getInt("id"));
                unit.setName(rs.getString("name"));
                unit.setSubName(rs.getString("sub_name"));
                unit.setDescription(rs.getString("description"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }

    // Get Unit by field (id, name, subName, or description)
    public List<Unit> getFilterUnit(String field, String searchText) {
        List<Unit> units = new ArrayList<>();
        try {
            String sql = "";
            switch (field) {
                case "id":
                    sql = "SELECT * FROM Units WHERE id=?";
                    break;
                case "name":
                    sql = "SELECT * FROM Units WHERE name LIKE CONCAT('%', ?, '%')";
                    break;
                case "sub_name":
                    sql = "SELECT * FROM Units WHERE sub_name LIKE CONCAT('%', ?, '%')";
                    break;
                case "description":
                    sql = "SELECT * FROM Units WHERE description LIKE CONCAT('%', ?, '%')";
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
                Unit unit = new Unit();
                unit.setId(rs.getInt("id"));
                unit.setName(rs.getString("name"));
                unit.setSubName(rs.getString("sub_name"));
                unit.setDescription(rs.getString("description"));
                units.add(unit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return units;
    }

    // Add Unit
    public void addUnit(Unit unit) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Units (name, sub_name, description) VALUES (?, ?, ?)"
            );
            ps.setString(1, unit.getName());
            ps.setString(2, unit.getSubName());
            ps.setString(3, unit.getDescription());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Unit
    public void updateUnit(Unit unit) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "UPDATE Units SET name=?, sub_name=?, description=? WHERE id=?"
            );
            ps.setString(1, unit.getName());
            ps.setString(2, unit.getSubName());
            ps.setString(3, unit.getDescription());
            ps.setInt(4, unit.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Unit
    public void deleteUnit(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM Units WHERE id=?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get Unit by ID
    public Unit getUnitById(int id) {
        Unit unit = new Unit();
        try {
            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Units WHERE id=?"
            );
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                unit.setId(rs.getInt("id"));
                unit.setName(rs.getString("name"));
                unit.setSubName(rs.getString("sub_name"));
                unit.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unit;
    }
}
