package com.inventoy.controller;

import com.inventory.dao.OpeningStockDAO;
import com.inventory.model.OpeningStock;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet("/openingStock")
public class OpeningStockServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OpeningStockDAO openingStockDAO;

    @Override
    public void init() throws ServletException {
        openingStockDAO = new OpeningStockDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String json = getOpeningStockData(request);
        
        try (PrintWriter out = response.getWriter()) {
            out.write(json);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);
        
        int productId = jsonObject.get("product_id").getAsInt();
        int stock = jsonObject.get("stock").getAsInt();
        Date date = new Date(jsonObject.get("date").getAsLong());
        String description = jsonObject.get("description").getAsString();
        
        OpeningStock openingStock = new OpeningStock(productId, stock, date, description);
        openingStockDAO.addOpeningStock(openingStock);
        
        writeSuccessResponse(response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);
        
        int id = jsonObject.get("id").getAsInt();
        int productId = jsonObject.get("product_id").getAsInt();
        int stock = jsonObject.get("stock").getAsInt();
        Date date = new Date(jsonObject.get("date").getAsLong());
        String description = jsonObject.get("description").getAsString();
        
        OpeningStock openingStock = new OpeningStock(productId, stock, date, description);
        openingStock.setId(id);
        openingStockDAO.updateOpeningStock(openingStock);
        
        writeSuccessResponse(response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JsonObject jsonObject = parseRequestData(request);
        
        int id = jsonObject.get("id").getAsInt();
        openingStockDAO.deleteOpeningStock(id);
        
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

    private String getOpeningStockData(HttpServletRequest request) {
        String id = request.getParameter("id");
        String productId = request.getParameter("product_id");
        String date = request.getParameter("date");
        String description = request.getParameter("description");
        
        Gson gson = new Gson();
        if (id != null) {
            return gson.toJson(openingStockDAO.getFilterOpeningStock("id", id));
        } else if (productId != null) {
            return gson.toJson(openingStockDAO.getFilterOpeningStock("product_id", productId));
        } else if (date != null) {
            return gson.toJson(openingStockDAO.getFilterOpeningStock("date", date));
        } else if (description != null) {
            return gson.toJson(openingStockDAO.getFilterOpeningStock("description", description));
        } else {
            return gson.toJson(openingStockDAO.getAllOpeningStocks());
        }
    }

    private void writeSuccessResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.write("{\"message\": \"success\"}");
        }
    }
}
