const apiOpeningStock = 'http://localhost:8080/Inventory_System/openingStock';
const apiProduct = 'http://localhost:8080/Inventory_System/product';

/* -------- Display Data Function -------- */
function displayData() {
    // Fetch all opening stock records
    fetch(apiOpeningStock)
        .then(response => response.json())
        .then(openingStocks => {
            // Fetch products to build a lookup map
            fetch(apiProduct)
                .then(response => response.json())
                .then(products => {
                    // Create a map for product ID to product name
                    const productMap = new Map(products.map(p => [p.id, p.name]));
                    
                    const columns = [
                        { title: "ID" },
                        { title: "Product" },
                        { title: "Stock" },
                        { title: "Date" },
                        { title: "Description" },
                        { title: "Option" }
                    ];
                    
                    const data = openingStocks.map(stock => {
                        const option = ` 
                            <button class="btn btn-info" data-bs-toggle="modal" data-bs-target="#myModal" onclick="editData(${stock.id})">Edit</button>
                            | 
                            <button class="btn btn-danger" onclick="deleteData(${stock.id})">Delete</button>
                        `;
                        return [
                            stock.id,
                            productMap.get(stock.productId) || 'N/A',
                            stock.stock,
                            new Date(stock.date).toLocaleDateString(),
                            stock.description ? stock.description.substring(0, 50) + (stock.description.length > 50 ? '...' : '') : '',
                            option
                        ];
                    });

                    $('#table_id').DataTable({
                        destroy: true,
                        data: data,
                        columns: columns
                    });
                })
                .catch(error => console.error('Error loading products:', error));
        })
        .catch(error => console.error('Error fetching opening stocks:', error));
}

/* -------- Load Product Dropdown -------- */
function loadProductDropdown() {
    const productSelect = $('#productId');
    productSelect.empty();
    productSelect.append('<option value="">--Select Product--</option>');
    fetch(apiProduct)
        .then(response => response.json())
        .then(products => {
            products.forEach(product => {
                productSelect.append(`<option value="${product.id}">${product.name}</option>`);
            });
        })
        .catch(error => console.error('Error loading products:', error));
}

/* -------- Document Ready -------- */
$(document).ready(function () {
    displayData();
    loadProductDropdown();
});

/* -------- Add New Button -------- */
$("#btnadd").click(function () {
    $("#id").val("");
    $("#productId").val("");
    $("#stock").val("");
    $("#date").val("");
    $("#description").val("");
    $("#btnsave").text("Insert");
});

/* -------- Save Button -------- */
$("#btnsave").click(function () {
    const productId = document.getElementById('productId').value;
    const stock = document.getElementById('stock').value;
    const date = document.getElementById('date').value;
    const description = document.getElementById('description').value;

    if ($("#btnsave").text() === "Insert") {
        // Insert new opening stock record
        fetch(apiOpeningStock, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                product_id: productId,
                stock: stock,
                date: new Date(date).getTime(),
                description: description
            })
        })
        .then(response => response.json())
        .then(data => {
            Swal.fire({ title: data.message, icon: "success" });
            displayData();
            $('#myModal').modal('hide');
        })
        .catch(error => console.error('Error inserting opening stock:', error));
    } else {
        // Update existing opening stock record
        fetch(apiOpeningStock, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                id: openingStock_id,
                product_id: productId,
                stock: stock,
                date: new Date(date).getTime(),
                description: description
            })
        })
        .then(response => response.json())
        .then(data => {
            Swal.fire({ title: data.message, icon: "success" });
            displayData();
            $('#myModal').modal('hide');
        })
        .catch(error => console.error('Error updating opening stock:', error));
    }
});

let openingStock_id; // Global variable for update operations

/* -------- Edit Data Function -------- */
function editData(id) {
    $("#btnsave").text("Update");
    openingStock_id = id;
    fetch(apiOpeningStock + "?id=" + id)
        .then(response => response.json())
        .then(data => {
            const stock = Array.isArray(data) ? data[0] : data;
            $("#id").val(stock.id);
            $("#productId").val(stock.productId);
            $("#stock").val(stock.stock);
            $("#date").val(new Date(stock.date).toISOString().split('T')[0]);
            $("#description").val(stock.description);
            $('#myModal').modal('show');
        })
        .catch(error => console.error('Error fetching stock data:', error));
}

/* -------- Delete Data Function -------- */
function deleteData(id) {
    Swal.fire({
        title: 'Do you want to remove this opening stock?',
        showDenyButton: true,
        confirmButtonText: 'Yes',
        denyButtonText: 'No',
        icon: "question",
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(apiOpeningStock, {
                method: 'DELETE',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ id: id })
            })
            .then(response => response.json())
            .then(data => {
                Swal.fire({ title: data.message, icon: "success" });
                displayData();
            })
            .catch(error => console.error('Error deleting opening stock:', error));
        } else if (result.isDenied) {
            Swal.fire('Opening stock not removed', '', 'info');
        }
    });
}
