// src/main/java/com/inventory/controller/LayoutServlet.java
package com.inventoy.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet("/layout")
public class LayoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(LayoutServlet.class.getName());

    public LayoutServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isAuthenticated(request)) {
            logger.warning("Unauthenticated access to /layout, redirecting to login.jsp");
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        logger.info("Processing /layout with action: " + action);
        if (action != null) {
            switch (action) {
                case "unit":
                    request.getRequestDispatcher("views/unit.jsp").forward(request, response);
                    break;
                case "brand":
                    request.getRequestDispatcher("views/brand.jsp").forward(request, response);
                    break;
                case "category":
                    request.getRequestDispatcher("views/category.jsp").forward(request, response);
                    break;
                case "product":
                    request.getRequestDispatcher("views/product.jsp").forward(request, response);
                    break;
                case "openingStock":
                    request.getRequestDispatcher("views/openingStock.jsp").forward(request, response);
                    break;
                case "supplier":
                    request.getRequestDispatcher("views/supplier.jsp").forward(request, response);
                    break;
                case "purchase":
                    request.getRequestDispatcher("views/purchase.jsp").forward(request, response);
                    break;
                case "customer":
                    request.getRequestDispatcher("views/customer.jsp").forward(request, response);
                    break;
                case "sell":
                    request.getRequestDispatcher("views/sell.jsp").forward(request, response);
                    break;
                case "adjuststock":
                    request.getRequestDispatcher("views/adjuststock.jsp").forward(request, response);
                    break;
                case "sell-summary":
                    request.getRequestDispatcher("views/sellsummary.jsp").forward(request, response);
                    break;
                case "users":
                    request.getRequestDispatcher("views/users.jsp").forward(request, response);
                    break;
                default:
                    request.getRequestDispatcher("views/index.jsp").forward(request, response);
                    break;
            }
        } else {
            request.getRequestDispatcher("views/index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean authenticated = session != null && session.getAttribute("userId") != null;
        logger.info("LayoutServlet authentication check: " + authenticated);
        return authenticated;
    }
}