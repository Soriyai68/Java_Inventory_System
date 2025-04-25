<!-- Chart Container -->
<div class="container mt-5">
    <div class="card shadow">
    
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">
                <div class="sidebar-brand">
                    <a href="layout?action=sell" style="text-decoration: none; color: black	; display: inline;">
                        <i class="fas fa-cube me-2"></i> Sales Chart
                    </a>
                </div>
            </h5>
        </div>
        <div class="card-body">
            <canvas id="salesChart" height="100"></canvas>
        </div>
    </div>
</div>

<!-- Chart.js CDN -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>
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
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Quantity Sold',
                        data: values,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: 'Quantity'
                            }
                        },
                        x: {
                            title: {
                                display: true,
                                text: 'Product'
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
