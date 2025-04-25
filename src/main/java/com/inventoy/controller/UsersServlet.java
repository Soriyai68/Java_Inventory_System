// src/main/java/com/inventory/controller/UsersServlet.java
package com.inventoy.controller;

import com.google.gson.*;
import com.inventory.dao.UsersDAO;
import com.inventory.model.Users;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

@WebServlet(urlPatterns = {"/login", "/users", "/logout"})
public class UsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UsersDAO usersDAO;
    private static final Logger logger = Logger.getLogger(UsersServlet.class.getName());
    private Gson gson;

    @Override
    public void init() {
        usersDAO = new UsersDAO();
        // Configure Gson with LocalDate adapter
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        gson = gsonBuilder.create();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        String path = request.getServletPath();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        logger.info("Received GET request for path: " + path);

        if ("/logout".equals(path)) {
            handleLogout(request, response);
        } else if ("/users".equals(path)) {
            if (!isAuthenticated(request)) {
                logger.warning("Unauthorized access to /users: No active session");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writeResponse(response, "{\"error\": \"Unauthorized: Please log in\"}");
                return;
            }
            if (!isAdmin(request)) {
                logger.warning("Forbidden access to /users: User is not admin");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                writeResponse(response, "{\"error\": \"Forbidden: Admin access required\"}");
                return;
            }
            handleGetUsers(request, response);
        } else {
            logger.warning("Invalid endpoint: " + path);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writeResponse(response, "{\"error\": \"Invalid endpoint\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        String path = request.getServletPath();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        logger.info("Received POST request for path: " + path);

        if ("/login".equals(path)) {
            handleLogin(request, response);
        } else if ("/users".equals(path)) {
            if (!isAuthenticated(request)) {
                logger.warning("Unauthorized access to /users: No active session");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writeResponse(response, "{\"error\": \"Unauthorized: Please log in\"}");
                return;
            }
            if (!isAdmin(request)) {
                logger.warning("Forbidden access to /users: User is not admin");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                writeResponse(response, "{\"error\": \"Forbidden: Admin access required\"}");
                return;
            }
            handleCreateUser(request, response);
        } else {
            logger.warning("Invalid endpoint: " + path);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writeResponse(response, "{\"error\": \"Invalid endpoint\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        String path = request.getServletPath();
        if (!"/users".equals(path)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writeResponse(response, "{\"error\": \"Invalid endpoint\"}");
            return;
        }
        if (!isAuthenticated(request)) {
            logger.warning("Unauthorized access to /users: No active session");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writeResponse(response, "{\"error\": \"Unauthorized: Please log in\"}");
            return;
        }
        if (!isAdmin(request)) {
            logger.warning("Forbidden access to /users: User is not admin");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writeResponse(response, "{\"error\": \"Forbidden: Admin access required\"}");
            return;
        }
        handleUpdateUser(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        String path = request.getServletPath();
        if (!"/users".equals(path)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writeResponse(response, "{\"error\": \"Invalid endpoint\"}");
            return;
        }
        if (!isAuthenticated(request)) {
            logger.warning("Unauthorized access to /users: No active session");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            writeResponse(response, "{\"error\": \"Unauthorized: Please log in\"}");
            return;
        }
        if (!isAdmin(request)) {
            logger.warning("Forbidden access to /users: User is not admin");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            writeResponse(response, "{\"error\": \"Forbidden: Admin access required\"}");
            return;
        }
        handleDeleteUser(request, response);
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String jsonData = readRequestBody(request);
            logger.info("Received login request");
            LoginRequest loginRequest = gson.fromJson(jsonData, LoginRequest.class);

            if (loginRequest == null || loginRequest.pin == null || loginRequest.pin.trim().isEmpty()) {
                logger.warning("Invalid login request: PIN is missing or empty");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"PIN is required\"}");
                return;
            }

            String pin = loginRequest.pin.trim();
            if (!pin.matches("^[0-9]{4,6}$")) {
                logger.warning("Invalid PIN format: [masked]");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"PIN must be 4-6 digits\"}");
                return;
            }

            Users user = usersDAO.getUserByPin(pin);
            if (user == null) {
                logger.warning("No user found for PIN: [masked]");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writeResponse(response, "{\"error\": \"Invalid PIN\"}");
                return;
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("role", user.getRole());
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            logger.info("Login successful for user ID: " + user.getId() + ", Role: " + user.getRole());

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "Login successful");
            responseJson.addProperty("role", user.getRole());
            writeResponse(response, gson.toJson(responseJson));
        } catch (JsonSyntaxException e) {
            logger.warning("Invalid JSON format: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse(response, "{\"error\": \"Invalid JSON format: " + escapeJson(e.getMessage()) + "\"}");
        } catch (Exception e) {
            logger.severe("Login failed: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeResponse(response, "{\"error\": \"Login failed: " + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleGetUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json;
        try {
            String id = request.getParameter("id");
            if (id != null) {
                try {
                    int userId = Integer.parseInt(id);
                    logger.info("Fetching user by ID: " + userId);
                    Users user = usersDAO.getUserById(userId);
                    if (user == null) {
                        logger.warning("User not found for ID: " + userId);
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        json = "{\"error\": \"User not found\"}";
                    } else {
                        json = gson.toJson(user);
                    }
                } catch (NumberFormatException e) {
                    logger.warning("Invalid ID format: " + id);
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    json = "{\"error\": \"Invalid ID format\"}";
                }
            } else {
                logger.info("Fetching all users");
                List<Users> users = usersDAO.getAllUsers();
                json = gson.toJson(users);
            }
        } catch (Exception e) {
            logger.severe("Failed to retrieve users: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json = "{\"error\": \"Failed to retrieve users: " + escapeJson(e.getMessage()) + "\"}";
        }
        writeResponse(response, json);
    }

    private void handleCreateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String jsonData = readRequestBody(request);
            logger.info("Create user request received");
            Users user = gson.fromJson(jsonData, Users.class);

            if (user.getPin() == null || user.getPin().trim().isEmpty()) {
                logger.warning("Invalid create user request: PIN is missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"PIN is required\"}");
                return;
            }

            if (!user.getPin().matches("^[0-9]{4,6}$")) {
                logger.warning("Invalid PIN format: [masked]");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"PIN must be 4-6 digits\"}");
                return;
            }

            if (user.getRole() == null || !isValidRole(user.getRole())) {
                logger.warning("Invalid role: " + user.getRole());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"Valid role is required (admin or user)\"}");
                return;
            }

            boolean created = usersDAO.addUser(user);
            if (!created) {
                logger.warning("PIN already exists: [masked]");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                writeResponse(response, "{\"error\": \"PIN already exists\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_CREATED);
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "User created");
            responseJson.add("user", gson.toJsonTree(user));
            writeResponse(response, gson.toJson(responseJson));
        } catch (JsonSyntaxException e) {
            logger.warning("Invalid JSON: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse(response, "{\"error\": \"Invalid JSON format\"}");
        } catch (Exception e) {
            logger.severe("Failed to create user: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeResponse(response, "{\"error\": \"Failed to create user: " + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleUpdateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String jsonData = readRequestBody(request);
            logger.info("Update user request received");
            Users user = gson.fromJson(jsonData, Users.class);

            if (user.getId() <= 0 || user.getPin() == null || user.getPin().trim().isEmpty()) {
                logger.warning("Invalid update user request: ID or PIN missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"Valid ID and PIN are required\"}");
                return;
            }

            if (!user.getPin().matches("^[0-9]{4,6}$")) {
                logger.warning("Invalid PIN format: [masked]");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"PIN must be 4-6 digits\"}");
                return;
            }

            if (user.getRole() == null || !isValidRole(user.getRole())) {
                logger.warning("Invalid role: " + user.getRole());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"Valid role is required (admin or user)\"}");
                return;
            }

            boolean updated = usersDAO.updateUser(user);
            if (!updated) {
                logger.warning("User not found for ID: " + user.getId());
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writeResponse(response, "{\"error\": \"User not found\"}");
                return;
            }

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "User updated");
            responseJson.add("user", gson.toJsonTree(user));
            writeResponse(response, gson.toJson(responseJson));
        } catch (JsonSyntaxException e) {
            logger.warning("Invalid JSON: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse(response, "{\"error\": \"Invalid JSON format\"}");
        } catch (Exception e) {
            logger.severe("Failed to update user: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeResponse(response, "{\"error\": \"Failed to update user: " + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String jsonData = readRequestBody(request);
            logger.info("Delete user request received");
            UserIdRequest requestBody = gson.fromJson(jsonData, UserIdRequest.class);

            if (requestBody.id <= 0) {
                logger.warning("Invalid delete user request: ID missing");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writeResponse(response, "{\"error\": \"Valid ID is required\"}");
                return;
            }

            boolean deleted = usersDAO.deleteUser(requestBody.id);
            if (!deleted) {
                logger.warning("User not found for ID: " + requestBody.id);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                writeResponse(response, "{\"error\": \"User not found\"}");
                return;
            }

            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("message", "User deleted successfully");
            writeResponse(response, gson.toJson(responseJson));
        } catch (JsonSyntaxException e) {
            logger.warning("Invalid JSON: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeResponse(response, "{\"error\": \"Invalid JSON format\"}");
        } catch (Exception e) {
            logger.severe("Failed to delete user: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            writeResponse(response, "{\"error\": \"Failed to delete user: " + escapeJson(e.getMessage()) + "\"}");
        }
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Processing logout request");
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            logger.info("Session invalidated");
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("message", "Logged out successfully");
        writeResponse(response, gson.toJson(responseJson));
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean authenticated = session != null && session.getAttribute("userId") != null;
        logger.info("Authentication check for /users: " + authenticated);
        return authenticated;
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        boolean isAdmin = session != null && "admin".equalsIgnoreCase((String) session.getAttribute("role"));
        logger.info("Admin check for /users: " + isAdmin);
        return isAdmin;
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }
        return jsonBuffer.toString();
    }

    private void writeResponse(HttpServletResponse response, String json) throws IOException {
        try (java.io.PrintWriter out = response.getWriter()) {
            out.print(json);
            out.flush();
        }
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    private boolean isValidRole(String role) {
        return role != null && (role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("user"));
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\"", "\\\"").replace("\n", "\\n");
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

        @Override
        public JsonElement serialize(LocalDate src, java.lang.reflect.Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(FORMATTER.format(src));
        }

        @Override
        public LocalDate deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), FORMATTER);
        }
    }

    private static class LoginRequest {
        String pin;
    }

    private static class UserIdRequest {
        int id;
    }
}