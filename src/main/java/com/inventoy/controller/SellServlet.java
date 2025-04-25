package com.inventoy.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import com.inventory.dao.SellDAO;
import com.inventory.model.Sell;
import com.inventory.model.Customer;
import com.inventory.model.Product;
import com.google.gson.Gson;

@WebServlet("/sell")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class SellServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SellDAO sellDAO;

    public void init() {
        sellDAO = new SellDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        try {
            String id = request.getParameter("id");
            if (id != null) {
                Sell sell = sellDAO.getSellById(Integer.parseInt(id));
                if (sell == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.write("{\"message\": \"Sale not found\"}");
                } else {
                    out.write(gson.toJson(sell));
                }
            } else {
                out.write(gson.toJson(sellDAO.getAllSells()));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Database error: " + e.getMessage() + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"Invalid sale ID\"}");
        } finally {
            out.flush();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            int productId = Integer.parseInt(request.getParameter("product_id"));
            int customerId = Integer.parseInt(request.getParameter("customer_id"));
            int saleStock = Integer.parseInt(request.getParameter("sale_stock"));
            double price = Double.parseDouble(request.getParameter("price"));
            double totalPrice = Double.parseDouble(request.getParameter("total_price"));
            String description = request.getParameter("description");
            String date = request.getParameter("date");

            Sell sell = new Sell();
            Product product = new Product();
            product.setId(productId);
            sell.setProduct(product);
            Customer customer = new Customer();
            customer.setId(customerId);
            sell.setCustomer(customer);
            sell.setSaleStock(saleStock);
            sell.setPrice(price);
            sell.setTotalPrice(totalPrice);
            sell.setDescription(description);
            sell.setDate(date);

            sellDAO.addSell(sell);
            out.write("{\"message\": \"Insert success\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Database error: " + e.getMessage() + "\"}");
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"Invalid input data\"}");
        } finally {
            out.flush();
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Sale ID is required\"}");
                return;
            }
            int id = Integer.parseInt(idParam);
            int productId = Integer.parseInt(request.getParameter("product_id"));
            int customerId = Integer.parseInt(request.getParameter("customer_id"));
            int saleStock = Integer.parseInt(request.getParameter("sale_stock"));
            double price = Double.parseDouble(request.getParameter("price"));
            double totalPrice = Double.parseDouble(request.getParameter("total_price"));
            String description = request.getParameter("description");
            String date = request.getParameter("date");

            Sell sell = new Sell();
            sell.setId(id);
            Product product = new Product();
            product.setId(productId);
            sell.setProduct(product);
            Customer customer = new Customer();
            customer.setId(customerId);
            sell.setCustomer(customer);
            sell.setSaleStock(saleStock);
            sell.setPrice(price);
            sell.setTotalPrice(totalPrice);
            sell.setDescription(description);
            sell.setDate(date);

            sellDAO.updateSell(sell);
            out.write("{\"message\": \"Update success\"}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Database error: " + e.getMessage() + "\"}");
        } catch (NumberFormatException | NullPointerException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"Invalid input data\"}");
        } finally {
            out.flush();
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Sale ID is required\"}");
                return;
            }
            int id = Integer.parseInt(idParam);
            boolean deleted = sellDAO.deleteSell(id);
            if (deleted) {
                out.write("{\"message\": \"Delete success\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"message\": \"Sale not found\"}");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Database error: " + e.getMessage() + "\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"Invalid sale ID\"}");
        } finally {
            out.flush();
        }
    }
}