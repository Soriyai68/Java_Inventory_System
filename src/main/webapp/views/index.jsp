<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Inventory Management</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts for Cyberpunk Font -->
    <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;700&family=Poppins:wght@400;500&display=swap" rel="stylesheet">
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

        /* Menu Toggle Button */
        .menu-toggle {
            position: fixed;
            top: 10px;
            left: 10px;
            z-index: 999;
            background: linear-gradient(90deg, rgba(0, 255, 204, 0.2), rgba(255, 0, 255, 0.2));
            border: 2px solid #ff00ff;
            color: #ffffff;
            padding: 10px 15px;
            font-size: 18px;
            border-radius: 8px;
            cursor: pointer;
            box-shadow: 0 0 15px rgba(255, 0, 255, 0.4);
            transition: all 0.3s ease;
        }

        .menu-toggle:hover {
            background: linear-gradient(90deg, rgba(0, 255, 204, 0.4), rgba(255, 0, 255, 0.4));
            transform: translateY(-2px);
            box-shadow: 0 0 25px rgba(255, 0, 255, 0.6);
            animation: glitch 0.5s ease;
        }

        .menu-toggle:focus {
            outline: none;
            box-shadow: 0 0 0 4px rgba(0, 255, 204, 0.4);
        }

        @keyframes glitch {
            0% { transform: translate(0); }
            20% { transform: translate(-2px, 2px); }
            40% { transform: translate(2px, -2px); }
            60% { transform: translate(-2px, 2px); }
            80% { transform: translate(2px, -2px); }
            100% { transform: translate(0); }
        }

        /* Sidebar Styling */
        .sidebar {
            width: 250px;
            height: 100vh;
            background: rgba(10, 10, 20, 0.9);
            border-right: 2px solid #00ffcc;
            color: #ffffff;
            position: fixed;
            top: 0;
            left: 0;
            overflow-y: auto;
            padding-top: 60px;
            transition: transform 0.3s ease-in-out;
            box-shadow: 0 0 20px rgba(0, 255, 204, 0.3);
        }

        .sidebar.collapsed {
            transform: translateX(-100%);
        }

        .sidebar-brand {
            font-size: 24px;
            text-align: center;
            padding: 20px 0;
            font-weight: 700;
            background: rgba(255, 0, 255, 0.1);
            border-bottom: 1px solid #ff00ff;
            color: #00ffcc;
            text-shadow: 0 0 10px rgba(0, 255, 204, 0.7);
        }

        .sidebar-brand a {
            color: #00ffcc;
            text-decoration: none;
        }

        .sidebar-nav a {
            display: block;
            padding: 15px 20px;
            color: #ffffff;
            text-decoration: none;
            font-family: 'Poppins', sans-serif;
            transition: background 0.3s, color 0.3s;
            border-left: 3px solid transparent;
        }

        .sidebar-nav a:hover {
            background: rgba(0, 255, 204, 0.2);
            color: #00ffcc;
            border-left: 3px solid #00ffcc;
            text-shadow: 0 0 5px rgba(0, 255, 204, 0.5);
        }

        .sidebar-nav i {
            margin-right: 10px;
            color: #ff00ff;
            text-shadow: 0 0 5px rgba(255, 0, 255, 0.5);
        }

        /* Main Content Styling */
        .main-content {
            margin-left: 250px;
            padding: 20px;
            flex-grow: 1;
            transition: margin-left 0.3s ease-in-out;
        }

        .main-content.collapsed {
            margin-left: 0;
        }

        /* Card Styling */
        .card {
            background: rgba(10, 10, 20, 0.8);
            border: 2px solid #00ffcc;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0, 255, 204, 0.3);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0 30px rgba(0, 255, 204, 0.5);
        }

        .card-header {
            background: rgba(255, 0, 255, 0.1);
            border-bottom: 1px solid #ff00ff;
            border-radius: 13px 13px 0 0;
            color: #00ffcc;
            text-shadow: 0 0 5px rgba(0, 255, 204, 0.5);
        }

        .card-header h5 {
            margin: 0;
            font-family: 'Orbitron', sans-serif;
            letter-spacing: 2px;
        }

        .card-body {
            padding: 30px;
            color: #ffffff;
            font-family: 'Poppins', sans-serif;
        }

        /* Chart Styling */
        canvas#salesChart {
            max-width: 100%;
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .sidebar {
                width: 200px;
            }

            .main-content {
                margin-left: 200px;
            }

            .main-content.collapsed {
                margin-left: 0;
            }

            .sidebar-brand {
                font-size: 20px;
                padding: 15px 0;
            }

            .sidebar-nav a {
                padding: 12px 15px;
                font-size: 0.9rem;
            }

            .card {
                padding: 1.5rem;
                border-radius: 12px;
            }

            .card-body {
                padding: 20px;
            }

            .card-header h5 {
                font-size: 1.2rem;
            }
        }

        @media (max-width: 576px) {
            .sidebar {
                width: 180px;
            }

            .main-content {
                margin-left: 180px;
            }

            .main-content.collapsed {
                margin-left: 0;
            }

            .sidebar-brand {
                font-size: 18px;
                padding: 10px 0;
            }

            .sidebar-nav a {
                padding: 10px 12px;
                font-size: 0.85rem;
            }

            .menu-toggle {
                padding: 8px 12px;
                font-size: 16px;
            }

            .card {
                padding: 1rem;
                border-radius: 10px;
            }

            .card-body {
                padding: 15px;
            }

            .card-header h5 {
                font-size: 1rem;
            }
        }
    </style>
</head>
<body>
    <button class="menu-toggle" onclick="toggleSidebar()">
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
            <a href="layout?action=users"><i class="fas fa-users"></i> Users</a>
            <a href="" onclick="logout()"><i class="fas fa-sign-out-alt"></i> Logout</a>
        </div>
    </div>

    <div class="main-content" id="mainContent">
        <div class="container mt-5">
            <div class="card">
                <div class="card-header text-white">
                    <h5 class="mb-0">Sales Chart Report</h5>
                </div>
                <div class="card-body">
                    <canvas id="salesChart" height="100"></canvas>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/gsap@3.11.1/dist/gsap.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="scripts/users.js"></script>
    <script>
        function toggleSidebar() {
            const sidebar = document.querySelector('.sidebar');
            const mainContent = document.querySelector('.main-content');
            const toggleIcon = document.querySelector('.menu-toggle i');

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
            gsap.from(".card-header h5", {
                duration: 0.8,
                opacity: 0,
                x: -20,
                ease: "power3.out"
            });
        });

        function loadSalesChart() {
            fetch('http://localhost:8080/Inventory_System/sell-summary')
                .then(response => {
                    if (!response.ok) throw new Error('Failed to fetch chart data');
                    return response.json();
                })
                .then(data => {
                    const labels = data.map(item => item.productName);
                    const values = data.map(item => item.totalQuantitySold);

                    const ctx = document.getElementById('salesChart').getContext('2d');

                    const gradient = ctx.createLinearGradient(0, 0, 0, 400);
                    gradient.addColorStop(0, 'rgba(0, 255, 204, 0.6)'); // Neon cyan
                    gradient.addColorStop(1, 'rgba(255, 0, 255, 0.2)'); // Neon magenta

                    new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: labels,
                            datasets: [{
                                label: 'Quantity Sold',
                                data: values,
                                backgroundColor: gradient,
                                borderColor: '#00ffcc',
                                borderWidth: 1,
                                borderRadius: 5
                            }]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                title: {
                                    display: true,
                                    text: 'Sell Summary by Product',
                                    font: {
                                        family: 'Orbitron',
                                        size: 18
                                    },
                                    color: '#00ffcc',
                                    padding: {
                                        bottom: 20
                                    }
                                },
                                legend: {
                                    display: false
                                }
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    title: {
                                        display: true,
                                        text: 'Quantity',
                                        font: {
                                            family: 'Poppins',
                                            size: 14
                                        },
                                        color: '#00ffcc'
                                    },
                                    grid: {
                                        color: 'rgba(0, 255, 204, 0.1)'
                                    },
                                    ticks: {
                                        color: '#ffffff',
                                        font: {
                                            family: 'Poppins'
                                        }
                                    }
                                },
                                x: {
                                    title: {
                                        display: true,
                                        text: 'Product',
                                        font: {
                                            family: 'Poppins',
                                            size: 14
                                        },
                                        color: '#00ffcc'
                                    },
                                    grid: {
                                        color: 'rgba(0, 255, 204, 0.1)'
                                    },
                                    ticks: {
                                        color: '#ffffff',
                                        font: {
                                            family: 'Poppins'
                                        }
                                    }
                                }
                            }
                        }
                    });
                })
                .catch(error => {
                    console.error('Chart error:', error);
                    alert('Failed to load chart data.');
                });
        }
        document.addEventListener("DOMContentLoaded", loadSalesChart);
    </script>
</body>
</html>