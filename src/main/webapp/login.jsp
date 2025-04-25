<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
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
            overflow: hidden;
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
            padding: 20px;
        }

        /* Card Styling */
        .card {
            background: rgba(10, 10, 20, 0.8);
            border: 2px solid #00ffcc;
            border-radius: 15px;
            padding: 2.5rem;
            box-shadow: 0 0 20px rgba(0, 255, 204, 0.3);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            height: 300px;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0 30px rgba(0, 255, 204, 0.5);
        }

        /* Heading Styling */
        h2 {
            color: #00ffcc;
            font-weight: 700;
            margin-bottom: 2rem;
            text-align: center;
            letter-spacing: 3px;
            font-size: 2rem;
            text-shadow: 0 0 10px rgba(0, 255, 204, 0.7);
        }

        /* Form Styling */
        .form-label {
            color: #00ffcc;
            font-weight: 400;
            margin-bottom: 0.5rem;
            font-size: 0.9rem;
            text-shadow: 0 0 5px rgba(0, 255, 204, 0.5);
            font-family: 'Poppins', sans-serif;
        }

        .form-control {
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid #ff00ff;
            border-radius: 8px;
            padding: 0.8rem;
            color: #ffffff; /* Explicitly set text color to white for PIN input */
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
            font-family: 'Poppins', sans-serif;
        }

        .form-control:focus {
            border-color: #00ffcc;
            box-shadow: 0 0 10px rgba(0, 255, 204, 0.5);
            background: rgba(255, 255, 255, 0.1);
            outline: none;
            color: #ffffff; /* Ensure focus state text remains white */
        }

        .form-control::placeholder {
            color: rgba(255, 255, 255, 0.4);
        }

        /* Button Styling */
        .btn-primary {
            background: linear-gradient(90deg, rgba(0, 255, 204, 0.2), rgba(255, 0, 255, 0.2));
            border: 2px solid #ff00ff;
            border-radius: 10px;
            padding: 0.8rem;
            font-weight: 500;
            text-transform: uppercase;
            letter-spacing: 2px;
            color: #ffffff;
            position: relative;
            box-shadow: 0 0 15px rgba(255, 0, 255, 0.4);
            transition: all 0.3s ease;
            width: 100%;
            overflow: hidden;
            font-family: 'Orbitron', sans-serif;
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

        @keyframes glitch {
            0% { transform: translate(0); }
            20% { transform: translate(-2px, 2px); }
            40% { transform: translate(2px, -2px); }
            60% { transform: translate(-2px, 2px); }
            80% { transform: translate(2px, -2px); }
            100% { transform: translate(0); }
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
        @media (max-width: 576px) {
            .container {
                padding: 10px;
            }

            .card {
                padding: 1.8rem;
                border-radius: 12px;
                height: 280px;
            }

            h2 {
                font-size: 1.4rem;
                margin-bottom: 1.5rem;
            }

            .form-control {
                padding: 0.7rem;
                font-size: 0.9rem;
            }

            .form-label {
                font-size: 0.85rem;
            }

            .btn-primary {
                padding: 0.7rem;
                font-size: 0.9rem;
            }

            .error-message {
                font-size: 0.8rem;
            }
        }
    </style>
</head>
<body>
    <div class="container d-flex align-items-center justify-content-center min-vh-100">
        <div class="col-md-4">
            <div class="card d-flex justify-content-center">
                <div class="w-100">
                    <h2 class="text-center mb-4">Login</h2>
                    <form id="loginForm">
                        <div class="mb-3">
                            <label for="pin" class="form-label">PIN</label>
                            <input type="password" class="form-control" id="pin" name="pin" placeholder="Enter PIN" required pattern="[0-9]+" autocomplete="off" aria-describedby="pinError">
                            <div id="pinError" class="error-message">Please enter a valid PIN (numbers only).</div>
                        </div>
                        <button type="submit" class="btn btn-primary" aria-label="Login">Login</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="scripts/users.js"></script>
    
</body>
</html>