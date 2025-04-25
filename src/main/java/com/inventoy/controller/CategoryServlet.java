package com.inventoy.controller;

import com.inventory.dao.CategoryDAO;
import com.inventory.model.Category;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/category")
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private CategoryDAO categoryDAO;

    public void init() {
        categoryDAO = new CategoryDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json;
        
        // Retrieve query parameters for filtering
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String description = request.getParameter("description");
        
        if (id != null) 
            json = gson.toJson(categoryDAO.getFilterCategory("id", id));
        else if (name != null)
            json = gson.toJson(categoryDAO.getFilterCategory("name", name));
        else if (code != null)
            json = gson.toJson(categoryDAO.getFilterCategory("code", code));
        else if (description != null)
            json = gson.toJson(categoryDAO.getFilterCategory("description", description));
        else 
            json = gson.toJson(categoryDAO.getAllCategories());
        
        PrintWriter out = response.getWriter();
        out.write(json);
        out.flush();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from the request
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
        int code = jsonObject.get("code").getAsInt();
        String description = jsonObject.get("description").getAsString();
        
        Category category = new Category(name, code, description);
        categoryDAO.addCategory(category);
      
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"success\"}");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from the request
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
        
        int id = jsonObject.get("category_id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        int code = jsonObject.get("code").getAsInt();
        String description = jsonObject.get("description").getAsString();
        
        Category category = new Category(name, code, description);
        category.setId(id);
        categoryDAO.updateCategory(category);
      
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"success\"}");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Read JSON data from the request
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
        categoryDAO.deleteCategory(id);
        
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"success\"}");
    }
}
