<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management</title>
    <link rel="stylesheet" href="assets/bootstrap5/css/bootstrap.min.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/dataTables/css/datatables.min.css">
    <link rel="stylesheet" href="assets/sweetalert2/css/sweetalert2.min.css">
    <!-- Google Fonts for Cyberpunk Font -->
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;700&family=Poppins:wght@400;500&display=swap" rel="stylesheet" media="print" onload="this.media='all'">
    <!-- GSAP Library -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/gsap/3.11.0/gsap.min.js"></script>
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
    font-family: 'Orbitron', sans-serif;
    overflow-x: hidden;
    position: relative;
    color: #ffffff;
    font-size: 14px; /* Reduced: Base font size for body */
}

/* Enhanced Neon Background */
body::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: linear-gradient(45deg, rgba(0, 255, 255, 0.15), rgba(255, 0, 255, 0.15), rgba(0, 255, 204, 0.15));
    opacity: 0.25;
    z-index: -1;
    animation: neonPulse 15s ease infinite;
}

@keyframes neonPulse {
    0%, 100% { opacity: 0.25; filter: blur(10px); }
    50% { opacity: 0.35; filter: blur(15px); }
}

/* Menu Toggle Button */
.menu-toggle {
    position: fixed;
    top: 15px;
    left: 15px;
    z-index: 999;
    background: linear-gradient(135deg, rgba(0, 255, 204, 0.3), rgba(255, 0, 255, 0.3));
    border: 2px solid #ff00ff;
    color: #ffffff;
    padding: 10px 16px; /* Reduced: Slightly smaller padding */
    font-size: clamp(14px, 2.2vw, 16px); /* Reduced: Smaller font size */
    border-radius: 10px;
    cursor: pointer;
    box-shadow: 0 0 20px rgba(255, 0, 255, 0.5);
    transition: all 0.3s ease;
    will-change: transform, box-shadow;
}

.menu-toggle:hover {
    background: linear-gradient(135deg, rgba(0, 255, 204, 0.5), rgba(255, 0, 255, 0.5));
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 0 30px rgba(255, 0, 255, 0.7);
    animation: glitch 0.4s ease;
}

.menu-toggle:focus {
    outline: none;
    box-shadow: 0 0 0 4px rgba(0, 255, 204, 0.5);
}

@keyframes glitch {
    0% { transform: translate(0); }
    20% { transform: translate(-3px, 3px); }
    40% { transform: translate(3px, -3px); }
    60% { transform: translate(-3px, 3px); }
    80% { transform: translate(3px, -3px); }
    100% { transform: translate(0); }
}

/* Sidebar Styling */
.sidebar {
    width: clamp(180px, 18vw, 260px); /* Reduced: Slightly narrower sidebar */
    height: 100vh;
    background: linear-gradient(180deg, rgba(10, 10, 20, 0.95), rgba(20, 20, 40, 0.95));
    border-right: 2px solid #00ffcc;
    color: #ffffff;
    position: fixed;
    top: 0;
    left: 0;
    overflow-y: auto;
    padding-top: 60px;
    transition: transform 0.3s ease-in-out;
    box-shadow: 0 0 25px rgba(0, 255, 204, 0.4);
}

.sidebar.collapsed {
    transform: translateX(-100%);
}

.sidebar-brand {
    font-size: clamp(18px, 2.8vw, 22px); /* Reduced: Smaller font size */
    text-align: center;
    padding: 18px 0; /* Reduced: Slightly less padding */
    font-weight: 700;
    background: rgba(255, 0, 255, 0.15);
    border-bottom: 1px solid #ff00ff;
    color: #00ffcc;
    text-shadow: 0 0 12px rgba(0, 255, 204, 0.8);
}

.sidebar-brand a {
    color: #00ffcc;
    text-decoration: none;
    transition: color 0.3s ease;
}

.sidebar-brand a:hover {
    color: #ff00ff;
}

.sidebar-nav a {
    display: flex;
    align-items: center;
    padding: 12px 18px; /* Reduced: Smaller padding */
    color: #ffffff;
    text-decoration: none;
    font-family: 'Poppins', sans-serif;
    font-size: clamp(12px, 1.8vw, 14px); /* Reduced: Smaller font size */
    transition: all 0.3s ease;
    border-left: 4px solid transparent;
}

.sidebar-nav a:hover, .sidebar-nav a[aria-current="page"] {
    background: rgba(0, 255, 204, 0.25);
    color: #00ffcc;
    border-left: 4px solid #00ffcc;
    text-shadow: 0 0 6px rgba(0, 255, 204, 0.6);
}

.sidebar-nav a:focus {
    outline: 2px solid #00ffcc;
    outline-offset: 2px;
    background: rgba(0, 255, 204, 0.25);
}

.sidebar-nav i {
    margin-right: 10px;
    color: #ff00ff;
    text-shadow: 0 0 6px rgba(255, 0, 255, 0.6);
    font-size: 14px; /* Reduced: Smaller icon size */
}

/* Main Content Styling */
.main-content {
    margin-left: clamp(180px, 18vw, 260px);
    padding: 25px 15px; /* Reduced: Slightly less padding */
    flex-grow: 1;
    transition: margin-left 0.3s ease-in-out;
}

.main-content.collapsed {
    margin-left: 0;
}

/* Card Styling */
.card {
    background: linear-gradient(145deg, rgba(10, 10, 20, 0.9), rgba(20, 20, 40, 0.9));
    border: 2px solid #00ffcc;
    border-radius: 12px; /* Reduced: Slightly smaller radius */
    box-shadow: 0 8px 32px rgba(0, 255, 204, 0.3);
    transition: transform 0.3s ease, box-shadow 0.3s ease;
    padding: 2rem; /* Reduced: Slightly less padding */
    will-change: transform, box-shadow;
}

.card:hover {
    transform: translateY(-8px);
    box-shadow: 0 12px 40px rgba(0, 255, 204, 0.5);
}

/* Heading Styling */
h2 {
    color: #00ffcc;
    font-weight: 700;
    margin-bottom: 1.5rem; /* Reduced: Slightly less margin */
    text-align: center;
    letter-spacing: 3px;
    font-size: clamp(1.4rem, 4vw, 2rem); /* Reduced: Smaller font size */
    text-shadow: 0 0 12px rgba(0, 255, 204, 0.8);
    animation: textGlow 3s ease-in-out infinite;
}

@keyframes textGlow {
    0%, 100% { text-shadow: 0 0 12px rgba(0, 255, 204, 0.8); }
    50% { text-shadow: 0 0 20px rgba(0, 255, 204, 1); }
}

/* Button Styling */
.btn {
    background: linear-gradient(135deg, rgba(0, 255, 204, 0.3), rgba(255, 0, 255, 0.3));
    border: 2px solid #ff00ff;
    border-radius: 10px; /* Reduced: Slightly smaller radius */
    padding: 0.7rem 1.4rem; /* Reduced: Smaller padding */
    font-weight: 500;
    text-transform: uppercase;
    letter-spacing: 1.5px; /* Reduced: Slightly less spacing */
    color: #ffffff;
    position: relative;
    box-shadow: 0 0 20px rgba(255, 0, 255, 0.5);
    transition: all 0.3s ease;
    overflow: hidden;
    font-family: 'Orbitron', sans-serif;
    font-size: clamp(11px, 1.6vw, 13px); /* Reduced: Smaller font size */
    will-change: transform, box-shadow;
}

.btn-success {
    background: linear-gradient(135deg, rgba(0, 255, 204, 0.4), rgba(0, 200, 255, 0.4));
    border: 2px solid #00ffcc;
    box-shadow: 0 0 20px rgba(0, 255, 204, 0.5);
}

.btn-danger {
    background: linear-gradient(135deg, rgba(255, 0, 102, 0.4), rgba(255, 0, 255, 0.4));
    border: 2px solid #ff0066;
    box-shadow: 0 0 20px rgba(255, 0, 102, 0.5);
}

.btn:hover, .btn-success:hover, .btn-danger:hover {
    background: linear-gradient(135deg, rgba(0, 255, 204, 0.6), rgba(255, 0, 255, 0.6));
    transform: translateY(-3px) scale(1.05);
    box-shadow: 0 0 30px rgba(255, 0, 255, 0.7);
    animation: glitch 0.4s ease;
}

.btn:focus, .btn-success:focus, .btn-danger:focus {
    outline: none;
    box-shadow: 0 0 0 4px rgba(0, 255, 204, 0.5);
}

.btn:active, .btn-success:active, .btn-danger:active {
    transform: scale(0.95);
    box-shadow: 0 0 12px rgba(255, 0, 255, 0.4);
}

/* Add New Product Button Styling */
#btnadd {
    width: clamp(200px, 22vw, 240px); /* Reduced: Slightly smaller width */
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    background: linear-gradient(135deg, #00ccff, #cc00ff);
    border: 2px solid #00ffff;
    border-radius: 10px;
    padding: 0.7rem 1.4rem; /* Reduced: Smaller padding */
    font-weight: 600;
    text-transform: uppercase;
    letter-spacing: 1.5px;
    color: #ffffff;
    transition: all 0.3s ease;
    font-family: 'Orbitron', sans-serif;
    box-shadow: 0 0 20px rgba(0, 204, 255, 0.6);
    position: relative;
    overflow: hidden;
    animation: flicker 2.5s ease-in-out infinite;
    font-size: clamp(11px, 1.6vw, 13px); /* Reduced: Smaller font size */
}

#btnadd::before {
    content: '';
    position: absolute;
    top: 0;
    left: -100%;
    width: 100%;
    height: 100%;
    background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
    transition: left 0.5s ease;
}

#btnadd:hover::before {
    left: 100%;
}

#btnadd:hover {
    background: linear-gradient(135deg, #cc00ff, #00ccff);
    transform: scale(1.05);
    box-shadow: 0 0 30px rgba(0, 204, 255, 0.9);
    border-color: #ff00ff;
    animation: none;
}

#btnadd:focus {
    outline: none;
    box-shadow: 0 0 0 4px rgba(0, 204, 255, 0.5);
    animation: none;
}

#btnadd:active {
    transform: scale(0.95);
    box-shadow: 0 0 12px rgba(0, 204, 255, 0.4);
}

/* Flicker Animation */
@keyframes flicker {
    0% { opacity: 1; box-shadow: 0 0 20px rgba(0, 204, 255, 0.6); }
    5% { opacity: 0.95; }
    10% { opacity: 1; }
    15% { opacity: 0.9; box-shadow: 0 0 12px rgba(0, 204, 255, 0.4); }
    20% { opacity: 1; }
    100% { opacity: 1; box-shadow: 0 0 20px rgba(0, 204, 255, 0.6); }
}

/* Centered Button Container */
.button-container {
    display: flex;
    justify-content: center;
    margin-bottom: 1.2rem; /* Reduced: Slightly less margin */
}

/* Table Styling */
.table {
    background: transparent;
    border: 1px solid #ff00ff;
    border-radius: 10px;
    color: #ffffff !important;
    width: 100%;
    table-layout: auto;
}

.table th, .table td {
    border: 1px solid #ff00ff;
    padding: 0.6rem; /* Reduced: Smaller padding */
    vertical-align: middle;
    text-align: center;
    font-family: 'Poppins', sans-serif;
    color: #ffffff !important;
    font-size: clamp(10px, 1.6vw, 12px); /* Reduced: Smaller font size */
}

.table-striped > tbody > tr:nth-of-type(odd) {
    background: rgba(255, 255, 255, 0.05);
}

.table-hover > tbody > tr:hover {
    background: rgba(0, 255, 204, 0.2);
    box-shadow: 0 0 12px rgba(0, 255, 204, 0.4);
}

/* DataTables Styling */
.dataTables_wrapper .dataTables_length,
.dataTables_wrapper .dataTables_filter,
.dataTables_wrapper .dataTables_info,
.dataTables_wrapper .dataTables_paginate {
    color: #ffffff !important;
    margin-bottom: 1.2rem; /* Reduced: Slightly less margin */
    font-size: clamp(10px, 1.6vw, 12px); /* Reduced: Smaller font size */
    font-family: 'Poppins', sans-serif;
}

.dataTables_wrapper .dataTables_filter input {
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid #ff00ff;
    border-radius: 8px; /* Reduced: Slightly smaller radius */
    color: #ffffff !important;
    padding: 0.5rem; /* Reduced: Smaller padding */
    width: 100%;
    max-width: clamp(120px, 18vw, 180px); /* Reduced: Slightly smaller width */
    font-size: clamp(10px, 1.6vw, 12px); /* Reduced: Smaller font size */
}

.dataTables_wrapper .dataTables_paginate .paginate_button {
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid #ff00ff;
    color: #ffffff !important;
    border-radius: 5px;
    margin: 0 3px;
    padding: 0.4rem 0.8rem; /* Reduced: Smaller padding */
    font-size: clamp(10px, 1.6vw, 12px); /* Reduced: Smaller font size */
}

.dataTables_wrapper .dataTables_paginate .paginate_button:hover {
    background: linear-gradient(135deg, #00ffcc, #ff00ff);
    border-color: #00ffcc;
    color: #ffffff !important;
}

.dataTables_wrapper .dataTables_paginate .paginate_button.current {
    background: linear-gradient(135deg, #00ffcc, #ff00ff);
    border-color: #00ffcc;
    color: #ffffff !important;
}

/* Modal Styling */
.modal-content {
    background: linear-gradient(145deg, rgba(10, 10, 20, 0.95), rgba(20, 20, 40, 0.95));
    border: 2px solid #00ffcc;
    border-radius: 12px;
    box-shadow: 0 8px 32px rgba(0, 255, 204, 0.4);
    color: #ffffff;
}

.modal-header {
    background: rgba(255, 0, 255, 0.15);
    border-bottom: 1px solid #ff00ff;
}

.modal-title {
    color: #00ffcc;
    font-weight: 700;
    letter-spacing: 1.5px;
    text-shadow: 0 0 6px rgba(0, 255, 204, 0.6);
    font-size: clamp(1rem, 3.5vw, 1.4rem); /* Reduced: Smaller font size */
}

.btn-close {
    filter: invert(1);
    background: rgba(255, 0, 255, 0.4);
    border-radius: 50%;
    transition: background 0.3s ease;
    padding: 8px; /* Reduced: Smaller padding */
}

.btn-close:hover {
    background: rgba(255, 0, 255, 0.6);
}

/* Form Styling */
.form-label {
    color: #00ffcc;
    font-weight: 400;
    margin-bottom: 0.5rem;
    font-size: clamp(10px, 1.6vw, 12px); /* Reduced: Smaller font size */
    text-shadow: 0 0 6px rgba(0, 255, 204, 0.6);
    font-family: 'Poppins', sans-serif;
}

.form-control, .form-select {
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid #ff00ff;
    border-radius: 8px;
    padding: 0.7rem; /* Reduced: Smaller padding */
    color: #ffffff;
    transition: border-color 0.3s ease, box-shadow 0.3s ease, background-color 0.3s ease;
    font-family: 'Poppins', sans-serif;
    font-size: clamp(10px, 1.6vw, 12px); /* Reduced: Smaller font size */
}

.form-control:focus, .form-select:focus {
    border-color: #00ffcc;
    box-shadow: 0 0 12px rgba(0, 255, 204, 0.6);
    background: rgba(0, 0, 0, 0.8);
    outline: none;
    color: #ffffff;
}

.form-select option:not(:disabled) {
    background: rgba(0, 0, 0, 0.8);
}

.form-select:valid:not(:placeholder-shown) {
    background: rgba(0, 0, 0, 0.8);
}

.form-control::placeholder, .form-select::placeholder {
    color: rgba(255, 255, 255, 0.5);
}

.form-text {
    color: rgba(255, 255, 255, 0.7);
    font-size: clamp(9px, 1.4vw, 11px); /* Reduced: Smaller font size */
}

.text-danger {
    color: #ff0066ソーシャルメディア
    text-shadow: 0 0 6px rgba(255, 0, 102, 0.6);
    font-size: clamp(9px, 1.4vw, 11px); /* Reduced: Smaller font size */
}

/* Price Input Wrapper */
.price-input-wrapper {
    position: relative;
}

.price-input-wrapper::before {
    content: "$";
    position: absolute;
    left: 12px; /* Reduced: Adjusted for smaller input */
    top: 50%;
    transform: translateY(-50%);
    color: #00ffcc;
    font-weight: 500;
    text-shadow: 0 0 6px rgba(0, 255, 204, 0.6);
    font-size: clamp(10px, 1.6vw, 12px); /* Reduced: Smaller font size */
}

.price-input-wrapper input {
    padding-left: 24px; /* Adjusted for smaller font */
}

/* Image Preview Styling */
#imagePreview img {
    max-width: 100%;
    height: auto;
    border: 1px solid #ff00ff;
    border-radius: 8px;
    margin-top: 0.5rem;
    box-shadow: 0 0 12px rgba(255, 0, 255, 0.5);
}

/* Accessibility: Reduced Motion */
@media (prefers-reduced-motion: reduce) {
    body::before, .btn, #btnadd, h2, .card, .sidebar-nav a {
        animation: none;
        transition: none;
    }
}

/* Responsive Design */

/* Large Screens (1200px and above) */
@media (min-width: 1200px) {
    .sidebar {
        width: 280px; /* Reduced: Slightly narrower */
    }

    .main-content {
        margin-left: 280px;
        padding: 35px;
    }

    .card {
        padding: 2.5rem;
        border-radius: 15px;
    }

    h2 {
        font-size: 2.2rem; /* Reduced: Smaller font size */
    }

    #btnadd {
        width: 260px;
        padding: 0.8rem 1.8rem;
    }

    .table th, .table td {
        padding: 0.8rem;
        font-size: 0.9rem; /* Reduced: Smaller font size */
    }

    .form-control, .form-select {
        padding: 0.8rem;
        font-size: 0.9rem; /* Reduced: Smaller font size */
    }

    .modal-dialog {
        max-width: 750px; /* Reduced: Slightly smaller modal */
    }
}

/* Medium Screens (768px and below) */
@media (max-width: 768px) {
    .sidebar {
        width: 200px;
    }

    .main-content {
        margin-left: 200px;
        padding: 15px;
    }

    .main-content.collapsed {
        margin-left: 0;
    }

    .sidebar-brand {
        font-size: 20px; /* Reduced: Smaller font size */
        padding: 12px 0;
    }

    .sidebar-nav a {
        padding: 10px 12px;
        font-size: 0.8rem; /* Reduced: Smaller font size */
    }

    .menu-toggle {
        padding: 8px 12px;
        font-size: 14px;
    }

    .card {
        padding: 1.5rem;
        border-radius: 10px;
    }

    h2 {
        font-size: 1.6rem; /* Reduced: Smaller font size */
        margin-bottom: 1rem;
    }

    #btnadd {
        width: 180px;
        padding: 0.6rem 1rem;
        font-size: 0.8rem; /* Reduced: Smaller font size */
    }

    .form-control, .form-select {
        padding: 0.6rem;
        font-size: 0.8rem; /* Reduced: Smaller font size */
    }

    .form-label {
        font-size: 0.75rem; /* Reduced: Smaller font size */
    }

    .table th, .table td {
        font-size: 0.75rem; /* Reduced: Smaller font size */
        padding: 0.5rem;
        color: #ffffff !important;
    }

    .dataTables_wrapper .dataTables_length,
    .dataTables_wrapper .dataTables_filter,
    .dataTables_wrapper .dataTables_info,
    .dataTables_wrapper .dataTables_paginate {
        font-size: 0.75rem; /* Reduced: Smaller font size */
        color: #ffffff !important;
    }

    .dataTables_wrapper .dataTables_filter input {
        max-width: 140px;
    }

    .table-responsive {
        overflow-x: auto;
        -webkit-overflow-scrolling: touch;
    }

    .modal-dialog {
        margin: 1rem;
        max-width: 90%;
    }
}

/* Small Screens (576px and below) */
@media (max-width: 576px) {
    .sidebar {
        width: 160px;
    }

    .main-content {
        margin-left: 160px;
        padding: 12px;
    }

    .main-content.collapsed {
        margin-left: 0;
    }

    .sidebar-brand {
        font-size: 16px; /* Reduced: Smaller font size */
        padding: 10px 0;
    }

    .sidebar-nav a {
        padding: 8px 10px;
        font-size: 0.75rem; /* Reduced: Smaller font size */
    }

    .menu-toggle {
        padding: 6px 10px;
        font-size: 12px;
        top: 8px;
        left: 8px;
    }

    .card {
        padding: 1rem;
        border-radius: 8px;
    }

    h2 {
        font-size: 1.2rem; /* Reduced: Smaller font size */
        letter-spacing: 2px;
    }

    #btnadd {
        width: 140px;
        padding: 0.5rem 0.8rem;
        font-size: 0.7rem; /* Reduced: Smaller font size */
    }

    .form-control, .form-select {
        padding: 0.5rem;
        font-size: 0.7rem; /* Reduced: Smaller font size */
    }

    .form-label {
        font-size: 0.7rem; /* Reduced: Smaller font size */
    }

    .table-responsive {
        overflow-x: auto;
        -webkit-overflow-scrolling: touch;
    }

    .modal-dialog {
        margin: 0.5rem;
        max-width: 95%;
    }

    .modal-content {
        border-radius: 10px;
    }

    .modal-title {
        font-size: 1rem; /* Reduced: Smaller font size */
    }

    /* Stack Table Columns */
    .table thead {
        display: none;
    }

    .table tr {
        display: block;
        margin-bottom: 0.8rem; /* Reduced: Smaller margin */
        border: 1px solid #ff00ff;
        border-radius: 6px;
        background: rgba(255, 255, 255, 0.05);
    }

    .table td {
        display: flex;
        justify-content: space-between;
        align-items: center;
        text-align: left;
        padding: 0.4rem 0.8rem; /* Reduced: Smaller padding */
        border: none;
        border-bottom: 1px solid rgba(255, 0, 255, 0.3);
        font-size: 0.7rem; /* Reduced: Smaller font size */
    }

    .table td::before {
        content: attr(data-label);
        font-weight: 500;
        color: #00ffcc;
        text-shadow: 0 0 6px rgba(0, 255, 204, 0.6);
        flex: 1;
        font-size: 0.7rem; /* Reduced: Smaller font size */
    }

    .table td:last-child {
        border-bottom: none;
    }
}

/* Very Small Screens (400px and below) */
@media (max-width: 400px) {
    .sidebar {
        width: 140px;
    }

    .main-content {
        margin-left: 140px;
        padding: 10px;
    }

    .main-content.collapsed {
        margin-left: 0;
    }

    .sidebar-brand {
        font-size: 14px; /* Reduced: Smaller font size */
        padding: 8px 0;
    }

    .sidebar-nav a {
        padding: 6px 8px;
        font-size: 0.7rem; /* Reduced: Smaller font size */
    }

    .menu-toggle {
        padding: 5px 8px;
        font-size: 11px;
    }

    .card {
        padding: 0.8rem;
        border-radius: 6px;
    }

    h2 {
        font-size: 1rem; /* Reduced: Smaller font size */
        letter-spacing: 1px;
    }

    #btnadd {
        width: 120px;
        padding: 0.4rem 0.6rem;
        font-size: 0.65rem; /* Reduced: Smaller font size */
    }

    .form-control, .form-select {
        padding: 0.4rem;
        font-size: 0.65rem; /* Reduced: Smaller font size */
    }

    .form-label {
        font-size: 0.65rem; /* Reduced: Smaller font size */
    }

    .table td {
        font-size: 0.65rem; /* Reduced: Smaller font size */
        padding: 0.3rem 0.6rem;
    }

    .dataTables_wrapper .dataTables_filter input {
        padding: 0.3rem;
        font-size: 0.65rem; /* Reduced: Smaller font size */
        max-width: 90px;
    }

    .dataTables_wrapper .dataTables_paginate .paginate_button {
        padding: 0.2rem 0.5rem;
        font-size: 0.65rem; /* Reduced: Smaller font size */
    }

    .modal-content {
        border-radius: 8px;
    }

    .modal-title {
        font-size: 0.9rem; /* Reduced: Smaller font size */
    }
}
    </style>
</head>
<body>
    <button class="menu-toggle" onclick="toggleSidebar()" aria-label="Toggle sidebar" aria-expanded="false">
        <i class="fas fa-bars"></i>
    </button>

    <div class="sidebar" id="sidebar">
        <div class="sidebar-brand">
            <a href="layout?action="><i class="fas fa-cube"></i> Inventory</a>
        </div>
        <div class="sidebar-nav">
            <a href="layout?action=product"><i class="fas fa-cube"></i> Products</a>
            <a href="layout?action=category"><i class="fas fa-th-large"></i> Category</a>
            <a href="layout?action=brand"><i class="fas fa-box"></i> Brands</a>
            <a href="layout?action=unit"><i class="fas fa-cogs"></i> Units</a>
            <a href="layout?action=openingStock"><i class="fas fa-warehouse"></i> Opening Stock</a>
            <a href="layout?action=supplier"><i class="fas fa-truck"></i> Supplier</a>
            <a href="layout?action=purchase"><i class="fas fa-shopping-cart"></i> Purchase</a>
            <a href="layout?action=customer"><i class="fas fa-users"></i> Customer</a>
            <a href="layout?action=sell"><i class="fas fa-cash-register"></i> Sell</a>
            <a href="layout?action=adjuststock"><i class="fas fa-sliders-h"></i> Adjust Stock</a>
            <a href="layout?action=sell-summary"><i class="fas fa-chart-bar"></i> Sales Chart</a>
           
        </div>
    </div>

    <div class="main-content">
        <div class="container">
            <div class="card">
                <h2>List of Products</h2>
                <div class="button-container">
                    <button type="button" id="btnadd" class="btn" data-bs-toggle="modal" data-bs-target="#myModal">
                        <i class="fas fa-plus"></i> Add Product
                    </button>
                </div>
                <div class="table-responsive">
                    <table id="table_id" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Product ID</th>
                                <th>Name</th>
                                <th>SKU</th>
                                <th>Brand</th>
                                <th>Category</th>
                                <th>Default Price</th>
                                <th>Selling Price</th>
                                <th>Image</th>
                                <th>Stock</th>
                                <th>Unit</th>
                                <th>Descriptions</th>
                                <th>Option</th>
                            </tr>
                        </thead>
                        <tbody></tbody>
                    </table>
                </div>
            </div>

            <div class="modal fade" id="myModal">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">Product Form</h4>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form id="productForm">
                                <input type="hidden" id="id">
                                <div class="mb-3">
                                    <label for="name" class="form-label">Product Name: <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="name" placeholder="Enter Product Name" name="name" required>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please enter a product name.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="sku" class="form-label">SKU: <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" id="sku" placeholder="Enter SKU" name="sku" required>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please enter a valid SKU.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="unitId" class="form-label">Unit: <span class="text-danger">*</span></label>
                                    <select class="form-select" id="unitId" name="unitId" required>
                                        <option value="" disabled selected>Select Unit</option>
                                    </select>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please select a unit.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="brandId" class="form-label">Brand: <span class="text-danger">*</span></label>
                                    <select class="form-select" id="brandId" name="brandId" required>
                                        <option value="" disabled selected>Select Brand</option>
                                    </select>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please select a brand.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="categoryId" class="form-label">Category: <span class="text-danger">*</span></label>
                                    <select class="form-select" id="categoryId" name="categoryId" required>
                                        <option value="" disabled selected>Select Category</option>
                                    </select>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please select a category.</div>
                                </div>
                                <div class="mb-3 price-input-wrapper">
                                    <label for="defaultPrice" class="form-label">Default Price: <span class="text-danger">*</span></label>
                                    <input type="number" step="0.01" class="form-control" id="defaultPrice" placeholder="Enter Default Price" name="defaultPrice" required>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please enter a valid default price.</div>
                                </div>
                                <div class="mb-3 price-input-wrapper">
                                    <label for="sellingPrice" class="form-label">Selling Price: <span class="text-danger">*</span></label>
                                    <input type="number" step="0.01" class="form-control" id="sellingPrice" placeholder="Enter Selling Price" name="sellingPrice" required>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please enter a valid selling price.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="image" class="form-label">Image:</label>
                                    <input type="file" class="form-control" id="image" name="image" accept="image/*">
                                    <div id="imagePreview" class="mt-2"></div>
                                </div>
                                <div class="mb-3">
                                    <label for="currentStock" class="form-label">Current Stock: <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" id="currentStock" name="currentStock" value="0" readonly required>
                                    <div class="invalid-feedback text-danger" style="display: none;">Please enter a valid stock quantity.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="descriptions" class="form-label">Descriptions:</label>
                                    <textarea class="form-control" id="descriptions" placeholder="Enter Product Descriptions" name="descriptions"></textarea>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-success" id="btnsave" aria-label="Save product">Save</button>
                            <button type="button" class="btn btn-danger" data-bs-dismiss="modal" aria-label="Close modal">Close</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="assets/jquery/jquery-3.7.1.min.js"></script>
    <script src="assets/bootstrap5/js/bootstrap.min.js"></script>
    <script src="assets/dataTables/js/datatables.min.js"></script>
    <script src="assets/sweetalert2/js/sweetalert2.all.min.js"></script>
    <script src="scripts/product.js"></script>
    <script>
        function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar');
            const mainContent = document.querySelector('.main-content');
            const toggleIcon = document.querySelector('.menu-toggle i');
            const toggleButton = document.querySelector('.menu-toggle');

            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('collapsed');

            if (sidebar.classList.contains('collapsed')) {
                gsap.to(toggleIcon, {
                    duration: 0.3,
                    rotation: -90,
                    ease: "power3.out",
                    onComplete: () => {
                        toggleIcon.classList.remove('fa-times');
                        toggleIcon.classList.add('fa-bars');
                        gsap.set(toggleIcon, { rotation: 0 });
                        toggleButton.setAttribute('aria-expanded', 'false');
                    }
                });
            } else {
                gsap.to(toggleIcon, {
                    duration: 0.3,
                    rotation: 90,
                    ease: "power3.out",
                    onComplete: () => {
                        toggleIcon.classList.remove('fa-bars');
                        toggleIcon.classList.add('fa-times');
                        gsap.set(toggleIcon, { rotation: 0 });
                        toggleButton.setAttribute('aria-expanded', 'true');
                    }
                });
            }
        }

        document.addEventListener("DOMContentLoaded", function() {
            gsap.from(".card", {
                duration: 0.8,
                opacity: 0,
                y: 20,
                ease: "power3.out"
            });
            gsap.from("h2", {
                duration: 0.8,
                opacity: 0,
                x: -20,
                ease: "power3.out"
            });
            gsap.from("#btnadd", {
                duration: 0.8,
                opacity: 0,
                y: 20,
                ease: "power3.out",
                delay: 0.2
            });

            // Image Preview
            document.getElementById('image').addEventListener('change', function(event) {
                const preview = document.getElementById('imagePreview');
                preview.innerHTML = '';
                const file = event.target.files[0];
                if (file) {
                    const img = document.createElement('img');
                    img.src = URL.createObjectURL(file);
                    preview.appendChild(img);
                }
            });
        });
    </script>
</body>
</html>