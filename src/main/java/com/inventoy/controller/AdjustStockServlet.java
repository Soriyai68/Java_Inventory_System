package com.inventoy.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.inventory.dao.AdjustStockDAO;
import com.inventory.model.AdjustStock;
import com.inventory.model.Product;
import com.google.gson.Gson;

@WebServlet("/adjuststock")
@MultipartConfig // Enable multipart/form-data processing
public class AdjustStockServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(AdjustStockServlet.class.getName());
    private AdjustStockDAO adjustStockDAO;
    private Gson gson;

    @Override
    public void init() {
        adjustStockDAO = new AdjustStockDAO();
        gson = new Gson();
        LOGGER.info("AdjustStockServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setResponseHeaders(response);
        PrintWriter out = response.getWriter();
        try {
            String idParam = request.getParameter("id");
            LOGGER.info("GET request received, id: " + idParam);
            
            if (idParam != null && !idParam.trim().isEmpty()) {
                int id = parseIntParam(idParam, "adjustment ID");
                AdjustStock adjustment = adjustStockDAO.getAdjustStockById(id);
                if (adjustment == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.println("{\"message\": \"Stock adjustment not found for ID: " + id + "\"}");
                } else {
                    out.println(gson.toJson(adjustment));
                }
            } else {
                out.println(gson.toJson(adjustStockDAO.getAllAdjustStocks()));
            }
        } catch (SQLException e) {
            handleSQLException(response, out, "GET", e);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"Invalid adjustment ID: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            handleUnexpectedException(response, out, "GET", e);
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setResponseHeaders(response);
        PrintWriter out = response.getWriter();
        try {
            LOGGER.info("POST request received with multipart data");
            
            // Extract form fields from multipart parts
            String productIdParam = getPartValue(request.getPart("product_id"));
            String adjustStockParam = getPartValue(request.getPart("adjust_stock"));
            String date = getPartValue(request.getPart("date"));
            String option = getPartValue(request.getPart("option"));
            String priceParam = getPartValue(request.getPart("price"));
            String totalPriceParam = getPartValue(request.getPart("total_price"));
            String description = getPartValue(request.getPart("description"));

            // Log received parameters
            LOGGER.info("Parameters: product_id=" + productIdParam + "; adjust_stock=" + adjustStockParam + 
                        "; date=" + date + "; option=" + option + "; price=" + priceParam + 
                        "; total_price=" + totalPriceParam + "; description=" + description);

            // Validate required fields
            if (isEmpty(productIdParam) || isEmpty(adjustStockParam) || isEmpty(date)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"message\": \"Required fields (product_id, adjust_stock, date) are missing or empty\"}");
                return;
            }

            // Parse and validate inputs
            int productId = parseIntParam(productIdParam, "product ID");
            int quantityAdjusted = parseIntParam(adjustStockParam, "adjust stock");
            if (quantityAdjusted == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"message\": \"Quantity adjusted cannot be zero\"}");
                return;
            }

            // Validate date format (YYYY-MM-DD)
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"message\": \"Invalid date format, expected YYYY-MM-DD\"}");
                return;
            }

            // Create AdjustStock object
            AdjustStock adjustment = new AdjustStock();
            Product product = new Product();
            product.setId(productId);
            adjustment.setProduct(product);
            adjustment.setOption(option);
            adjustment.setAdjustStock(quantityAdjusted);
            adjustment.setPrice(parseDoubleParam(priceParam, "price", 0.0));
            adjustment.setTotalPrice(parseDoubleParam(totalPriceParam, "total price", 0.0));
            adjustment.setDate(date);
            adjustment.setDescription(description);

            adjustStockDAO.addAdjustStock(adjustment);
            response.setStatus(HttpServletResponse.SC_CREATED);
            out.println("{\"message\": \"Stock adjustment added successfully\"}");
        } catch (SQLException e) {
            handleSQLException(response, out, "POST", e);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"Invalid numeric input: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            handleUnexpectedException(response, out, "POST", e);
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setResponseHeaders(response);
        PrintWriter out = response.getWriter();
        try {
            LOGGER.info("PUT request received with multipart data");
            
            // Extract id from query parameter, others from multipart parts
            String idParam = request.getParameter("id"); // Changed to query param
            String productIdParam = getPartValue(request.getPart("product_id"));
            String adjustStockParam = getPartValue(request.getPart("adjust_stock"));
            String date = getPartValue(request.getPart("date"));
            String option = getPartValue(request.getPart("option"));
            String priceParam = getPartValue(request.getPart("price"));
            String totalPriceParam = getPartValue(request.getPart("total_price"));
            String description = getPartValue(request.getPart("description"));

            // Log received parameters
            LOGGER.info("Parameters: id=" + idParam + "; product_id=" + productIdParam + "; adjust_stock=" + adjustStockParam + 
                        "; date=" + date + "; option=" + option + "; price=" + priceParam + 
                        "; total_price=" + totalPriceParam + "; description=" + description);

            // Validate required fields
            if (isEmpty(idParam) || isEmpty(productIdParam) || isEmpty(adjustStockParam) || isEmpty(date)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"message\": \"Required fields (id, product_id, adjust_stock, date) are missing or empty\"}");
                return;
            }

            // Parse and validate inputs
            int id = parseIntParam(idParam, "adjustment ID");
            int productId = parseIntParam(productIdParam, "product ID");
            int quantityAdjusted = parseIntParam(adjustStockParam, "adjust stock");
            if (quantityAdjusted == 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"message\": \"Quantity adjusted cannot be zero\"}");
                return;
            }

            // Validate date format
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"message\": \"Invalid date format, expected YYYY-MM-DD\"}");
                return;
            }

            // Create AdjustStock object
            AdjustStock adjustment = new AdjustStock();
            adjustment.setId(id);
            Product product = new Product();
            product.setId(productId);
            adjustment.setProduct(product);
            adjustment.setOption(option);
            adjustment.setAdjustStock(quantityAdjusted);
            adjustment.setPrice(parseDoubleParam(priceParam, "price", 0.0));
            adjustment.setTotalPrice(parseDoubleParam(totalPriceParam, "total price", 0.0));
            adjustment.setDate(date);
            adjustment.setDescription(description);

            adjustStockDAO.updateAdjustStock(adjustment);
            response.setStatus(HttpServletResponse.SC_OK);
            out.println("{\"message\": \"Stock adjustment updated successfully\"}");
        } catch (SQLException e) {
            handleSQLException(response, out, "PUT", e);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"Invalid numeric input: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            handleUnexpectedException(response, out, "PUT", e);
        } finally {
            out.flush();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setResponseHeaders(response);
        PrintWriter out = response.getWriter();
        try {
            String idParam = request.getParameter("id");
            LOGGER.info("DELETE request received, id: " + idParam);
            
            if (isEmpty(idParam)) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println("{\"message\": \"Adjustment ID is required\"}");
                return;
            }

            int id = parseIntParam(idParam, "adjustment ID");
            boolean deleted = adjustStockDAO.deleteAdjustStock(id);
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println("{\"message\": \"Stock adjustment deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println("{\"message\": \"Stock adjustment not found for ID: " + id + "\"}");
            }
        } catch (SQLException e) {
            handleSQLException(response, out, "DELETE", e);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("{\"message\": \"Invalid adjustment ID: " + e.getMessage() + "\"}");
        } catch (Exception e) {
            handleUnexpectedException(response, out, "DELETE", e);
        } finally {
            out.flush();
        }
    }

    // Utility Methods
    private void setResponseHeaders(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // CORS headers disabled since JSP and servlet are same-origin
        // response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        // response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        // response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private int parseIntParam(String param, String fieldName) throws NumberFormatException {
        try {
            return Integer.parseInt(param.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid " + fieldName + ": " + param);
        }
    }

    private double parseDoubleParam(String param, String fieldName, double defaultValue) {
        if (isEmpty(param)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(param.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid " + fieldName + ": " + param);
        }
    }

    private void handleSQLException(HttpServletResponse response, PrintWriter out, String method, SQLException e) {
        LOGGER.severe("SQLException in " + method + ": " + e.getMessage() + ", SQLState: " + e.getSQLState());
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.println("{\"message\": \"Database error: " + e.getMessage() + "\"}");
    }

    private void handleUnexpectedException(HttpServletResponse response, PrintWriter out, String method, Exception e) {
        LOGGER.severe("Unexpected error in " + method + ": " + e.getMessage());
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        out.println("{\"message\": \"Unexpected error: " + e.getMessage() + "\"}");
    }

    private String getPartValue(Part part) throws IOException {
        if (part == null) {
            return null;
        }
        byte[] buffer = new byte[(int) part.getSize()];
        part.getInputStream().read(buffer);
        return new String(buffer, "UTF-8").trim();
    }
}