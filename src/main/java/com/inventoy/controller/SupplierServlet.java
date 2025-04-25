package com.inventoy.controller;

import com.inventory.dao.SupplierDAO;
import com.inventory.model.Supplier;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/supplier")
public class SupplierServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SupplierDAO supplierDAO;

    @Override
    public void init() throws ServletException {
        supplierDAO = new SupplierDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json = getSupplierData(request);

        try (PrintWriter out = response.getWriter()) {
            out.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);

        String name = jsonObject.get("name").getAsString();
        String phone = jsonObject.get("phone").getAsString();
        String email = jsonObject.get("email").getAsString();

        Supplier supplier = new Supplier(name, phone, email);
        supplierDAO.addSupplier(supplier);

        writeSuccessResponse(response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);

        int id = jsonObject.get("id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String phone = jsonObject.get("phone").getAsString();
        String email = jsonObject.get("email").getAsString();

        Supplier supplier = new Supplier(name, phone, email);
        supplier.setId(id);
        supplierDAO.updateSupplier(supplier);

        writeSuccessResponse(response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);

        int id = jsonObject.get("id").getAsInt();
        supplierDAO.deleteSupplier(id);

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

    private String getSupplierData(HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");

        Gson gson = new Gson();
        if (id != null) {
            return gson.toJson(supplierDAO.getFilterSuppliers("id", id));
        } else if (name != null) {
            return gson.toJson(supplierDAO.getFilterSuppliers("name", name));
        } else if (phone != null) {
            return gson.toJson(supplierDAO.getFilterSuppliers("phone", phone));
        } else if (email != null) {
            return gson.toJson(supplierDAO.getFilterSuppliers("email", email));
        } else {
            return gson.toJson(supplierDAO.getAllSuppliers());
        }
    }

    private void writeSuccessResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.write("{\"message\": \"success\"}");
        }
    }
}
