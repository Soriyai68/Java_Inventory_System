package com.inventoy.controller;

import com.inventory.dao.CustomerDAO;
import com.inventory.model.Customer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CustomerDAO customerDAO;

    @Override
    public void init() throws ServletException {
        customerDAO = new CustomerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = getCustomerData(request);

        try (PrintWriter out = response.getWriter()) {
            out.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);

        String name = jsonObject.get("name").getAsString();
        long phone = jsonObject.get("phone").getAsLong();
        String email = jsonObject.get("email").getAsString();

        Customer customer = new Customer(name, phone, email);
        customerDAO.addCustomer(customer);

        writeSuccessResponse(response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);

        int id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        long phone = jsonObject.get("phone").getAsLong();
        String email = jsonObject.get("email").getAsString();

        Customer customer = new Customer(name, phone, email);
        customer.setId(id);
        customerDAO.updateCustomer(customer);

        writeSuccessResponse(response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);

        int id = jsonObject.get("id").getAsInt();
        customerDAO.deleteCustomer(id);

        writeSuccessResponse(response);
    }

    private JsonObject parseRequestData(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }
        String jsonData = jsonBuffer.toString();
        JsonParser parser = new JsonParser();
        return parser.parse(jsonData).getAsJsonObject();
    }

    private String getCustomerData(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        Gson gson = new Gson();
        if (id != null) {
            return gson.toJson(customerDAO.getFilterCustomers("id", id));
        } else if (name != null) {
            return gson.toJson(customerDAO.getFilterCustomers("name", name));
        } else if (phone != null) {
            return gson.toJson(customerDAO.getFilterCustomers("phone", phone));
        } else if (email != null) {
            return gson.toJson(customerDAO.getFilterCustomers("email", email));
        } else {
            return gson.toJson(customerDAO.getAllCustomers());
        }
    }

    private void writeSuccessResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.write("{\"message\": \"success\"}");
        }
    }
}