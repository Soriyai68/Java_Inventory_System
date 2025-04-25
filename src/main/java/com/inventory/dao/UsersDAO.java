// src/main/java/com/inventory/dao/UsersDAO.java
package com.inventory.dao;

import com.inventory.model.Users;
import com.inventory.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UsersDAO {
    private static final Logger logger = Logger.getLogger(UsersDAO.class.getName());

    public Users getUserByPin(String pin) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE pin = ?")) {
            ps.setString(1, pin);
            logger.info("Executing getUserByPin with PIN: [masked]");
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users user = mapResultSetToUser(rs);
                    logger.info("Found user: ID=" + user.getId());
                    return user;
                }
                logger.warning("No user found for PIN: [masked]");
                return null;
            }
        } catch (SQLException e) {
            logger.severe("Failed to retrieve user by PIN: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve user", e);
        }
    }

    public List<Users> getAllUsers() {
        List<Users> users = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            logger.info("Retrieved " + users.size() + " users");
        } catch (SQLException e) {
            logger.severe("Failed to retrieve users: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve users", e);
        }
        return users;
    }

    public Users getUserById(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
                logger.warning("No user found for ID: " + id);
                return null;
            }
        } catch (SQLException e) {
            logger.severe("Failed to retrieve user by ID: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve user", e);
        }
    }

    public boolean addUser(Users user) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Users (pin, role, created_at) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getPin());
            ps.setString(2, user.getRole());
            ps.setDate(3, user.getCreatedAt() != null ? java.sql.Date.valueOf(user.getCreatedAt()) : null);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                        logger.info("Added user: ID=" + user.getId());
                        return true;
                    }
                }
            }
            logger.warning("Failed to add user: PIN=" + user.getPin());
            return false;
        } catch (SQLException e) {
            logger.severe("Failed to add user: " + e.getMessage());
            throw new RuntimeException("Failed to add user", e);
        }
    }

    public boolean updateUser(Users user) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Users SET pin = ?, role = ?, created_at = ? WHERE id = ?")) {
            ps.setString(1, user.getPin());
            ps.setString(2, user.getRole());
            ps.setDate(3, user.getCreatedAt() != null ? java.sql.Date.valueOf(user.getCreatedAt()) : null);
            ps.setInt(4, user.getId());
            int rowsAffected = ps.executeUpdate();
            logger.info("Updated user: ID=" + user.getId() + ", Rows affected=" + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Failed to update user: " + e.getMessage());
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public boolean deleteUser(int id) {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Users WHERE id = ?")) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted user: ID=" + id + ", Rows affected=" + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Failed to delete user: " + e.getMessage());
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    private Users mapResultSetToUser(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setId(rs.getInt("id"));
        user.setPin(rs.getString("pin"));
        String role = rs.getString("role");
        user.setRole(role != null ? role : "user");
        java.sql.Date createdAt = rs.getDate("created_at");
        user.setCreatedAt(createdAt != null ? createdAt.toLocalDate() : LocalDate.now());
        return user;
    }
}