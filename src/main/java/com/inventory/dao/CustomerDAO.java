package com.inventory.dao;

import com.inventory.model.Customer;
import com.inventory.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private Connection connection;

    public CustomerDAO() {
        connection = DBUtil.getConnection();
    }

    /**
     * Retrieves all customer records.
     */
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM Customers";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getInt("id"));
                customer.setName(rs.getString("name"));
                customer.setPhone(rs.getLong("phone"));
                customer.setEmail(rs.getString("email"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Retrieves customer records filtered by a specific field and search text.
     * Supported fields: "id", "name", "phone", "email".
     */
    public List<Customer> getFilterCustomers(String field, String searchText) {
        List<Customer> customers = new ArrayList<>();
        String sql = "";
        switch (field) {
            case "id":
                sql = "SELECT * FROM Customers WHERE id = ?";
                break;
            case "name":
                sql = "SELECT * FROM Customers WHERE name LIKE CONCAT('%', ?, '%')";
                break;
            case "phone":
                sql = "SELECT * FROM Customers WHERE phone = ?";
                break;
            case "email":
                sql = "SELECT * FROM Customers WHERE email LIKE CONCAT('%', ?, '%')";
                break;
            default:
                return customers;
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if ("id".equals(field)) {
                ps.setInt(1, Integer.parseInt(searchText));
            } else if ("phone".equals(field)) {
                ps.setLong(1, Long.parseLong(searchText));
            } else {
                ps.setString(1, searchText);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("id"));
                    customer.setName(rs.getString("name"));
                    customer.setPhone(rs.getLong("phone"));
                    customer.setEmail(rs.getString("email"));
                    customers.add(customer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Inserts a new Customer record.
     */
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO Customers (name, phone, email) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setLong(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing Customer record.
     */
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE Customers SET name = ?, phone = ?, email = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, customer.getName());
            ps.setLong(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setInt(4, customer.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a Customer record.
     */
    public void deleteCustomer(int id) {
        String sql = "DELETE FROM Customers WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a single Customer record by its ID.
     */
    public Customer getCustomerById(int id) {
        Customer customer = new Customer();
        String sql = "SELECT * FROM Customers WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer.setId(rs.getInt("id"));
                    customer.setName(rs.getString("name"));
                    customer.setPhone(rs.getLong("phone"));
                    customer.setEmail(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }
}