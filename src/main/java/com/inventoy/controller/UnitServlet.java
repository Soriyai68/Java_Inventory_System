package com.inventoy.controller;

import com.inventory.dao.UnitDAO;
import com.inventory.model.Unit;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/unit")
public class UnitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private UnitDAO unitDAO;

    public void init() {
        unitDAO = new UnitDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json;
        
        // Retrieve query parameters for filtering
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String subName = request.getParameter("sub_name");
        String description = request.getParameter("description");
        
        if (id != null) 
            json = gson.toJson(unitDAO.getFilterUnit("id", id));
        else if (name != null)
            json = gson.toJson(unitDAO.getFilterUnit("name", name));
        else if (subName != null)
            json = gson.toJson(unitDAO.getFilterUnit("sub_name", subName));
        else if (description != null)
            json = gson.toJson(unitDAO.getFilterUnit("description", description));
        else 
            json = gson.toJson(unitDAO.getAllUnits());
        
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
        String subName = jsonObject.get("sub_name").getAsString();
        String description = jsonObject.get("description").getAsString();
        
        Unit unit = new Unit(name, subName, description);
        unitDAO.addUnit(unit);
      
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
        
        int id = jsonObject.get("unit_id").getAsInt();
        String name = jsonObject.get("name").getAsString();
        String subName = jsonObject.get("sub_name").getAsString();
        String description = jsonObject.get("description").getAsString();
        
        Unit unit = new Unit(name, subName, description);
        unit.setId(id);
        unitDAO.updateUnit(unit);
      
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
        unitDAO.deleteUnit(id);
        
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"success\"}");
    }
}
