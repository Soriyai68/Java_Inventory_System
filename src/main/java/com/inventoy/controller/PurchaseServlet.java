package com.inventoy.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import com.inventory.dao.PurchaseDAO;
import com.inventory.model.Purchase;
import com.inventory.model.Supplier;
import com.inventory.model.Product;
import com.google.gson.Gson;

@WebServlet("/purchase")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class PurchaseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private PurchaseDAO purchaseDAO;

    public void init() {
        purchaseDAO = new PurchaseDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = "";
        String id = request.getParameter("id");
        if (id != null) {
            json = gson.toJson(purchaseDAO.getPurchaseById(Integer.parseInt(id)));
        } else {
            json = gson.toJson(purchaseDAO.getAllPurchases());
        }
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int supplierId = Integer.parseInt(request.getParameter("supplier"));
        int productId = Integer.parseInt(request.getParameter("product"));
        String date = request.getParameter("date");
        String status = request.getParameter("status");
        int stock = Integer.parseInt(request.getParameter("stock"));
        double costPrice = Double.parseDouble(request.getParameter("cost_price"));
        String description = request.getParameter("description");

        Purchase purchase = new Purchase();
        Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        purchase.setSupplier(supplier);
        Product product = new Product();
        product.setId(productId);
        purchase.setProduct(product);
        purchase.setDate(date);
        purchase.setStatus(status);
        purchase.setStock(stock);
        purchase.setCost_price(costPrice);
        purchase.setDescription(description);

        purchaseDAO.addPurchase(purchase);

        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"insert success\"}");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("purchase_id"));
        int supplierId = Integer.parseInt(request.getParameter("supplier"));
        int productId = Integer.parseInt(request.getParameter("product"));
        String date = request.getParameter("date");
        String status = request.getParameter("status");
        int stock = Integer.parseInt(request.getParameter("stock"));
        double costPrice = Double.parseDouble(request.getParameter("cost_price"));
        String description = request.getParameter("description");

        Purchase purchase = new Purchase();
        purchase.setId(id);
        Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        purchase.setSupplier(supplier);
        Product product = new Product();
        product.setId(productId);
        purchase.setProduct(product);
        purchase.setDate(date);
        purchase.setStatus(status);
        purchase.setStock(stock);
        purchase.setCost_price(costPrice);
        purchase.setDescription(description);

        purchaseDAO.updatePurchase(purchase);

        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"update success\"}");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Purchase ID is required\"}");
                return;
            }
            int id = Integer.parseInt(idParam);
            boolean deleted = purchaseDAO.deletePurchase(id);
            if (deleted) {
                out.write("{\"message\": \"delete success\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"message\": \"Failed to delete purchase\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"Invalid purchase ID\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Error deleting purchase: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}