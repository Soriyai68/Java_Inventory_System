package com.inventoy.controller;

import com.inventory.dao.SellSummaryDAO;
import com.inventory.model.SellSummary;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/sell-summary")
public class SellSummaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SellSummaryDAO sellSummaryDAO;

    @Override
    public void init() throws ServletException {
        sellSummaryDAO = new SellSummaryDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            List<SellSummary> sellSummaryList = sellSummaryDAO.getSellSummaryData();
            Gson gson = new Gson();

            if (sellSummaryList.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"message\": \"No sell summary data found\"}");
            } else {
                out.print(gson.toJson(sellSummaryList));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"message\": \"Database error: " + e.getMessage() + "\"}");
        }
    }
}
