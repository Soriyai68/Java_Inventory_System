package com.inventoy.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import com.inventory.model.Unit;
import com.inventory.model.Brand;
import com.inventory.model.Category;
import com.google.gson.Gson;

@WebServlet("/product")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
                 maxFileSize = 1024 * 1024 * 10,      // 10MB
                 maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class ProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String UPLOAD_DIR = "Uploads";

    private ProductDAO productDAO;

    public void init() {
        productDAO = new ProductDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        String json = "";
        String id = request.getParameter("id");
        if (id != null) 
            json = gson.toJson(productDAO.getProductById(Integer.parseInt(id)));
        else 
            json = gson.toJson(productDAO.getAllProducts());
        PrintWriter out = response.getWriter();
        out.print(json);
        out.flush();
    }

    private String uploadImage(HttpServletRequest request) throws IOException, ServletException {
        String uploadPath = Paths.get(getServletContext().getRealPath(""), UPLOAD_DIR).toString();
        System.out.println("Upload Path: " + uploadPath);

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdir();
            System.out.println("Upload directory created: " + created);
        }

        Part filePart = request.getPart("image");
        if (filePart == null) {
            System.out.println("No file part found in the request.");
            return null;
        }

        String fileName = filePart.getSubmittedFileName();
        System.out.println("File Name: " + fileName);

        if (fileName == null || fileName.isEmpty()) {
            System.out.println("No file uploaded.");
            return null;
        }

        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);
        System.out.println("File saved to: " + filePath);

        return UPLOAD_DIR + File.separator + fileName;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String sku = request.getParameter("sku");
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        int brandId = Integer.parseInt(request.getParameter("brandId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        double defaultPrice = Double.parseDouble(request.getParameter("defaultPrice"));
        double sellingPrice = Double.parseDouble(request.getParameter("sellingPrice"));
        String imageUrl = uploadImage(request);
        int currentStock = Integer.parseInt(request.getParameter("currentStock"));
        String descriptions = request.getParameter("descriptions");

        Product product = new Product();
        product.setName(name);
        product.setSku(sku);
        Unit unit = new Unit();
        unit.setId(unitId);
        product.setUnit(unit);
        Brand brand = new Brand();
        brand.setId(brandId);
        product.setBrand(brand);
        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);
        product.setDefaultPrice(defaultPrice);
        product.setSellingPrice(sellingPrice);
        product.setImageUrl(imageUrl);
        product.setCurrentStock(currentStock);
        product.setDescriptions(descriptions);

        productDAO.addProduct(product);

        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"insert success\"}");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String sku = request.getParameter("sku");
        int unitId = Integer.parseInt(request.getParameter("unitId"));
        int brandId = Integer.parseInt(request.getParameter("brandId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        double defaultPrice = Double.parseDouble(request.getParameter("defaultPrice"));
        double sellingPrice = Double.parseDouble(request.getParameter("sellingPrice"));
        String imageUrl = uploadImage(request);
        int currentStock = Integer.parseInt(request.getParameter("currentStock"));
        String descriptions = request.getParameter("descriptions");

        Product existingProduct = productDAO.getProductById(id);
        if (imageUrl == null) {
            imageUrl = existingProduct.getImageUrl();
        }

        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setSku(sku);
        Unit unit = new Unit();
        unit.setId(unitId);
        product.setUnit(unit);
        Brand brand = new Brand();
        brand.setId(brandId);
        product.setBrand(brand);
        Category category = new Category();
        category.setId(categoryId);
        product.setCategory(category);
        product.setDefaultPrice(defaultPrice);
        product.setSellingPrice(sellingPrice);
        product.setImageUrl(imageUrl);
        product.setCurrentStock(currentStock);
        product.setDescriptions(descriptions);

        productDAO.updateProduct(product);

        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"update success\"}");
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"message\": \"Product ID is required\"}");
                return;
            }
            int id = Integer.parseInt(idParam);
            boolean deleted = productDAO.deleteProduct(id);
            if (deleted) {
                out.write("{\"message\": \"delete success\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"message\": \"Failed to delete product\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"message\": \"Invalid product ID\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"message\": \"Error deleting product: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}