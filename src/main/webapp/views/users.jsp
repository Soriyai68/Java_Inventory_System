<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Users - Inventory System</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- DataTables CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    <!-- DataTables Responsive CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/responsive/2.5.0/css/responsive.bootstrap5.min.css">
    <!-- SweetAlert2 CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <!-- Google Fonts for Cyberpunk Font -->
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;700&display=swap" rel="stylesheet">
    <style>
        /* General Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Body Styling */
        body {
            background: linear-gradient(135deg, #0d0d1a, #1a1a2e);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Orbitron', sans-serif;
            overflow-x: hidden;
            padding: 10px;
            position: relative;
        }

        /* Neon Background Animation */
        body::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: linear-gradient(45deg, rgba(0, 255, 255, 0.1), rgba(255, 0, 255, 0.1));
            opacity: 0.2;
            z-index: -1;
            animation: neonGlow 10s ease infinite;
        }

        @keyframes neonGlow {
            0%, 100% { opacity: 0.2; }
            50% { opacity: 0.4; }
        }

        /* Container Styling */
        .container {
            padding: 15px;
            max-width: 100%;
            width: 100%;
            margin: 0 auto;
        }

        /* Card Styling */
        .card {
            background: rgba(10, 10, 20, 0.8);
            border: 2px solid #00ffcc;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 0 20px rgba(0, 255, 204, 0.3);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0 30px rgba(0, 255, 204, 0.5);
        }

        /* Heading Styling */
        h2 {
            color: #00ffcc;
            font-weight: 700;
            margin-bottom: 1.5rem;
            text-align: center;
            letter-spacing: 3px;
            font-size: 2rem;
            text-shadow: 0 0 10px rgba(0, 255, 204, 0.7);
        }

        /* Cyberpunk Button Styling */
        .btn-primary {
            background: linear-gradient(90deg, rgba(0, 255, 204, 0.2), rgba(255, 0, 255, 0.2));
            border: 2px solid #ff00ff;
            border-radius: 10px;
            padding: 0.8rem 1.5rem;
            font-weight: 500;
            text-transform: uppercase;
            letter-spacing: 2px;
            color: #ffffff;
            position: relative;
            box-shadow: 0 0 15px rgba(255, 0, 255, 0.4);
            transition: all 0.3s ease;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            width: 240px;
            overflow: hidden;
        }

        .btn-primary:hover {
            background: linear-gradient(90deg, rgba(0, 255, 204, 0.4), rgba(255, 0, 255, 0.4));
            transform: translateY(-2px);
            box-shadow: 0 0 25px rgba(255, 0, 255, 0.6);
            animation: glitch 0.5s ease;
        }

        .btn-primary:focus {
            outline: none;
            box-shadow: 0 0 0 4px rgba(0, 255, 204, 0.4);
        }

        .btn-primary:active {
            transform: scale(0.95);
            box-shadow: 0 0 10px rgba(255, 0, 255, 0.3);
        }

        .btn-primary i {
            font-size: 0.9rem;
            text-shadow: 0 0 5px rgba(255, 255, 255, 0.7);
        }

        @keyframes glitch {
            0% { transform: translate(0); }
            20% { transform: translate(-2px, 2px); }
            40% { transform: translate(2px, -2px); }
            60% { transform: translate(-2px, 2px); }
            80% { transform: translate(2px, -2px); }
            100% { transform: translate(0); }
        }

        /* Centered Button Container */
        .button-container {
            display: flex;
            justify-content: center;
            margin-bottom: 1rem;
        }

        /* Form Styling */
        .form-label {
            color: #00ffcc;
            font-weight: 400;
            margin-bottom: 0.5rem;
            font-size: 0.9rem;
            text-shadow: 0 0 5px rgba(0, 255, 204, 0.5);
        }

        .form-control, .form-select {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid #ff00ff;
            border-radius: 8px;
            padding: 0.7rem;
            color: #ffffff;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
            font-family: 'Poppins', sans-serif;
        }

        .form-control:focus, .form-select:focus {
            border-color: #00ffcc;
            box-shadow: 0 0 10px rgba(0, 255, 204, 0.5);
            background: rgba(255, 255, 255, 0.1);
            outline: none;
        }

        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.4);
        }

        /* Table Styling */
        .table {
            background: transparent;
            border: 1px solid #ff00ff;
            border-radius: 10px;
            color: #ffffff;
            width: 100%;
            table-layout: auto;
        }

        .table th, .table td {
            border: 1px solid #ff00ff;
            padding: 0.7rem;
            vertical-align: middle;
            text-align: center;
            font-family: 'Poppins', sans-serif;
        }

        .table th {
            background: rgba(255, 0, 255, 0.1);
            text-transform: uppercase;
            letter-spacing: 2px;
            font-weight: 500;
            color: #00ffcc;
            text-shadow: 0 0 5px rgba(0, 255, 204, 0.5);
        }

        .table-striped > tbody > tr:nth-of-type(odd) {
            background: rgba(255, 255, 255, 0.02);
        }

        .table-hover > tbody > tr:hover {
            background: rgba(0, 255, 204, 0.1);
            box-shadow: 0 0 10px rgba(0, 255, 204, 0.3);
        }

        /* DataTables Customizations */
        .dataTables_wrapper .dataTables_length,
        .dataTables_wrapper .dataTables_filter,
        .dataTables_wrapper .dataTables_info,
        .dataTables_wrapper .dataTables_paginate {
            color: #00ffcc;
            margin-bottom: 1rem;
            font-size: 0.9rem;
            font-family: 'Poppins', sans-serif;
        }

        .dataTables_wrapper .dataTables_filter input {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid #ff00ff;
            border-radius: 8px;
            color: #ffffff;
            padding: 0.5rem;
            width: 100%;
            max-width: 200px;
        }

        .dataTables_wrapper .dataTables_paginate .paginate_button {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid #ff00ff;
            color: #ffffff !important;
            border-radius: 5px;
            margin: 0 3px;
            padding: 0.4rem 0.8rem;
        }

        .dataTables_wrapper .dataTables_paginate .paginate_button:hover {
            background: linear-gradient(90deg, #00ffcc, #ff00ff);
            border-color: #00ffcc;
            color: #ffffff !important;
        }

        .dataTables_wrapper .dataTables_paginate .paginate_button.current {
            background: linear-gradient(90deg, #00ffcc, #ff00ff);
            border-color: #00ffcc;
            color: #ffffff !important;
        }

        /* Enhanced DataTables Responsive Styling */
        .dataTables_wrapper .dt-buttons .btn {
            background: linear-gradient(90deg, #00ffcc, #ff00ff);
            border: none;
            border-radius: 10px;
            color: #ffffff;
            padding: 0.5rem 1rem;
            width: 240px;
            font-family: 'Orbitron', sans-serif;
        }

        .dataTables_wrapper .dt-buttons .btn:hover {
            background: linear-gradient(90deg, #00ccaa, #cc00cc);
            box-shadow: 0 0 20px rgba(255, 0, 255, 0.5);
        }

        .dataTables_wrapper .dataTable .child {
            background: rgba(10, 10, 20, 0.8);
            border: 1px solid #ff00ff;
            color: #ffffff;
            padding: 0.5rem;
            font-family: 'Poppins', sans-serif;
        }

        .dataTables_wrapper .dataTable .child ul {
            list-style: none;
            padding: 0;
        }

        .dataTables_wrapper .dataTable .child ul li {
            padding: 0.5rem 0;
            border-bottom: 1px solid #ff00ff;
            color: #00ffcc;
        }

        /* Modal Styling */
        .modal-content {
            background: rgba(10, 10, 20, 0.9);
            border: 2px solid #00ffcc;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0, 255, 204, 0.4);
            color: #ffffff;
        }

        .modal-header {
            background: rgba(255, 0, 255, 0.1);
            border-bottom: 1px solid #ff00ff;
        }

        .modal-title {
            color: #00ffcc;
            font-weight: 700;
            letter-spacing: 2px;
            text-shadow: 0 0 5px rgba(0, 255, 204, 0.5);
        }

        .btn-close {
            filter: invert(1);
            background: rgba(255, 0, 255, 0.3);
            border-radius: 50%;
        }

        /* Error Message Styling */
        .error-message {
            color: #ff0066;
            font-size: 0.85rem;
            margin-top: 0.3rem;
            display: none;
            font-weight: 400;
            text-shadow: 0 0 5px rgba(255, 0, 102, 0.5);
            font-family: 'Poppins', sans-serif;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .container {
                padding: 10px;
            }

            .card {
                padding: 1.5rem;
                border-radius: 12px;
            }

            h2 {
                font-size: 1.5rem;
                margin-bottom: 1rem;
            }

            .btn-primary {
                padding: 0.6rem 1rem;
                font-size: 0.9rem;
                width: 200px;
            }

            .dataTables_wrapper .dt-buttons .btn {
                width: 200px;
            }

            .form-control, .form-select {
                padding: 0.6rem;
                font-size: 0.9rem;
            }

            .form-label {
                font-size: 0.85rem;
            }

            .table th, .table td {
                font-size: 0.85rem;
                padding: 0.5rem;
            }

            .dataTables_wrapper .dataTables_length,
            .dataTables_wrapper .dataTables_filter,
            .dataTables_wrapper .dataTables_info,
            .dataTables_wrapper .dataTables_paginate {
                font-size: 0.85rem;
            }

            .dataTables_wrapper .dataTables_filter input {
                max-width: 150px;
            }

            .modal-dialog {
                margin: 10px;
            }

            .modal-content {
                padding: 1rem;
            }
        }

        @media (max-width: 576px) {
            .card {
                padding: 1rem;
                border-radius: 10px;
            }

            h2 {
                font-size: 1.2rem;
            }

            .btn-primary {
                padding: 0.5rem 0.8rem;
                font-size: 0.8rem;
                width: 160px;
            }

            .dataTables_wrapper .dt-buttons .btn {
                width: 160px;
            }

            .form-control, .form-select {
                padding: 0.5rem;
                font-size: 0.8rem;
            }

            .form-label {
                font-size: 0.8rem;
            }

            .table th, .table td {
                font-size: 0.8rem;
                padding: 0.4rem;
            }

            .dataTables_wrapper .dataTables_filter input {
                padding: 0.4rem;
                font-size: 0.8rem;
                max-width: 120px;
            }

            .dataTables_wrapper .dataTables_paginate .paginate_button {
                padding: 0.3rem 0.6rem;
                font-size: 0.8rem;
            }
        }
    </style>
</head>
<body>

    <div class="container">
     <div class="sidebar-brand">
            <a href="layout?action="><i class="fas fa-cube"></i> Inventory</a>
        </div>
        <br/>
        <div class="card">
            <h2>Users</h2>
            
            <div class="button-container">
                <button id="btnadd" class="btn btn-primary mb-3" aria-label="Add new user"><i class="fas fa-plus"></i> Add New</button>
            </div>
            <table id="table_id" class="table table-striped table-bordered"></table>
        </div>
    </div>

    <!-- Modal for Add/Edit User -->
    <div class="modal fade" id="userModal" tabindex="-1" aria-labelledby="userModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="userModalLabel">User Form</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <form id="userForm">
                        <div class="mb-3">
                            <label for="pin" class="form-label">PIN</label>
                            <input type="text" class="form-control" id="pin" placeholder="Enter PIN" required pattern="[0-9]+" autocomplete="off" aria-describedby="pinError">
                            <div id="pinError" class="error-message">Please enter a valid PIN (numbers only).</div>
                        </div>
                        <div class="mb-3">
                            <label for="role" class="form-label">Role</label>
                            <select class="form-select" id="role" required>
                                <option value="" disabled selected>Select Role</option>
                                <option value="user">User</option>
                                <option value="admin">Admin</option>
                            </select>
                        </div>
                        <div class="button-container">
                            <button type="button" id="btnsave" class="btn btn-primary" aria-label="Save user"><i class="fas fa-save"></i> Insert</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- jQuery (must be loaded first) -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>
    <!-- DataTables Responsive JS -->
    <script src="https://cdn.datatables.net/responsive/2.5.0/js/dataTables.responsive.min.js"></script>
    <script src="https://cdn.datatables.net/responsive/2.5.0/js/responsive.bootstrap5.min.js"></script>
    <!-- SweetAlert2 JS -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <!-- Custom Script -->
    <script src="scripts/users.js"></script>
    <script>
        $(document).ready(function() {
            $("#userForm").on("submit", function(event) {
                const pin = $("#pin").val();
                const pinError = $("#pinError");

                if (!/^[0-9]+$/.test(pin)) {
                    event.preventDefault();
                    pinError.show();
                    return false;
                } else {
                    pinError.hide();
                }
            });

            $("#pin").on("input", function() {
                const pinError = $("#pinError");
                if (this.value && !/^[0-9]+$/.test(this.value)) {
                    pinError.show();
                } else {
                    pinError.hide();
                }
            });
        });
    </script>
</body>
</html>