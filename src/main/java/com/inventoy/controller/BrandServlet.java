package com.inventoy.controller;

import com.inventory.dao.BrandDAO;
import com.inventory.model.Brand;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/brand")
public class BrandServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private BrandDAO brandDAO;

    public void init() {
        brandDAO = new BrandDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json;
        
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        if (id != null) 
            json = gson.toJson(brandDAO.getFilterBrand("id", id));
        else if (name != null)
            json = gson.toJson(brandDAO.getFilterBrand("name", name));
        else if (description != null)
            json = gson.toJson(brandDAO.getFilterBrand("description", description));
        else 
            json = gson.toJson(brandDAO.getAllBrands());
        
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        String jsonData = jsonBuffer.toString();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        
        Brand brand = new Brand(name, description);
        brandDAO.addBrand(brand);
      
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"success\"}");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }
        
        String jsonData = jsonBuffer.toString();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();
        
        int id = jsonObject.get("brand_id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String description = jsonObject.get("description").getAsString();
        
        Brand brand = new Brand(name, description);
        brand.setId(id);
        brandDAO.updateBrand(brand);
      
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"success\"}");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }
        
        String jsonData = jsonBuffer.toString();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject();
        
        int id = jsonObject.get("id").getAsInt();
        brandDAO.deleteBrand(id);
        
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"success\"}");
    }
}
