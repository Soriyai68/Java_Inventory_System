const apiUrl = 'http://localhost:8080/Inventory_System/purchase';
const productApiUrl = 'http://localhost:8080/Inventory_System/product';
const supplierApiUrl = 'http://localhost:8080/Inventory_System/supplier';

/*--------displayData Function------*/
function displayData() {
    fetch(apiUrl)
    .then(response => {
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    })
    .then(purchases => {
        console.log('Purchases fetched:', purchases);
        var data = [];
        var option = '';
        purchases.forEach(purchase => {
            option = `
                <button class="btn btn-edit" data-bs-toggle="modal" data-bs-target="#myModal" onclick="editData(${purchase.id})">
                    <i class="fas fa-pen"></i> Edit
                </button>
                <button class="btn btn-delete" onclick="deleteData(${purchase.id})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            `;
            data.push([
                purchase.id,
                purchase.supplier && purchase.supplier.name ? purchase.supplier.name : 'N/A',
                purchase.product && purchase.product.name ? purchase.product.name : 'N/A',
                purchase.date,
                purchase.status,
                purchase.stock,
                purchase.cost_price ? '$' + parseFloat(purchase.cost_price).toFixed(2) : '$0.00',
                purchase.description ? purchase.description.substring(0, 50) + (purchase.description.length > 50 ? '...' : '') : '',
                option
            ]);
        });
        console.log('Data for DataTable:', data);

        $('#table_id').DataTable({
            destroy: true,
            data: data,
            columns: [
                { title: "Purchase ID" },
                { title: "Supplier" },
                { title: "Product" },
                { title: "Date" },
                { title: "Status" },
                { title: "Stock" },
                { title: "Cost Price" },
                { title: "Description" },
                { title: "Option" }
            ],
            columnDefs: [
                { targets: 8, className: 'option-column' } // Option column (index 8)
            ]
        });
    })
    .catch(error => {
        console.error('Error in displayData:', error);
        Swal.fire({
            title: 'Error Loading Purchases',
            text: error.message,
            icon: 'error'
        });
    });
}

/*-------Query Load------*/
$(document).ready(function () {
    displayData();
    loadDropdowns();
});

/*------Load Dropdowns for Supplier and Product------*/
function loadDropdowns() {
    fetch(supplierApiUrl)
    .then(response => response.json())
    .then(suppliers => {
        let supplierSelect = $('#supplier');
        supplierSelect.empty();
        supplierSelect.append('<option value="">Select Supplier</option>');
        suppliers.forEach(supplier => {
            supplierSelect.append(`<option value="${supplier.id}">${supplier.name}</option>`);
        });
    })
    .catch(error => console.error('Error loading suppliers:', error));

    fetch(productApiUrl)
    .then(response => response.json())
    .then(products => {
        let productSelect = $('#product');
        productSelect.empty();
        productSelect.append('<option value="">Select Product</option>');
        products.forEach(product => {
            productSelect.append(`<option value="${product.id}">${product.name}</option>`);
        });
    })
    .catch(error => console.error('Error loading products:', error));
}

/*------AddNew Button--------*/
$("#btnadd").click(function() {
    $("#supplier").val("");
    $("#product").val("");
    $("#date").val("");
    $("#status").val("");
    $("#stock").val("");
    $("#cost_price").val("");
    $("#description").val("");
    $("#btnsave").text("Insert");
});

/*-------Save Button--------*/
$("#btnsave").click(function() {
    const formData = new FormData();
    formData.append('supplier', document.getElementById('supplier').value);
    formData.append('product', document.getElementById('product').value);
    formData.append('date', document.getElementById('date').value);
    formData.append('status', document.getElementById('status').value);
    formData.append('stock', document.getElementById('stock').value);
    formData.append('cost_price', document.getElementById('cost_price').value);
    formData.append('description', document.getElementById('description').value);

    if ($("#btnsave").text() === "Insert") {
        fetch(apiUrl, {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            Swal.fire({ title: data.message, icon: "success" });
            displayData();
            $('#myModal').modal('hide');
        })
        .catch(error => console.error('Error:', error));
    } else {
        formData.append('purchase_id', purchase_id);
        fetch(apiUrl, {
            method: 'PUT',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            Swal.fire({ title: data.message, icon: "success" });
            displayData();
            $('#myModal').modal('hide');
        })
        .catch(error => console.error('Error:', error));
    }
});

var purchase_id;
/*--------edit button-------*/
function editData(id) {
    $("#btnsave").text("Update");
    purchase_id = id;
    fetch(apiUrl + "?id=" + id)
    .then(response => response.json())
    .then(data => {
        document.getElementById("supplier").value = data.supplier.id;
        document.getElementById("product").value = data.product.id;
        document.getElementById("date").value = data.date;
        document.getElementById("status").value = data.status;
        document.getElementById("stock").value = data.stock;
        document.getElementById("cost_price").value = data.cost_price;
        document.getElementById("description").value = data.description || '';
    })
    .catch(error => console.error('Error:', error));
}

/*------delete button--------*/
function deleteData(id) {
    Swal.fire({ 
        title: 'Do you want to remove this purchase?', 
        showDenyButton: true, 
        confirmButtonText: 'Yes', 
        denyButtonText: 'No', 
        icon: "question", 
    }).then((result) => { 
        if (result.isConfirmed) { 
            fetch(apiUrl + '?id=' + id, {
                method: 'DELETE'
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => { throw new Error(err.message || 'Deletion failed'); });
                }
                return response.json();
            })
            .then(data => {
                Swal.fire({ title: data.message, icon: "success" });
                displayData();
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire({
                    title: 'Failed to delete purchase',
                    text: error.message,
                    icon: 'error'
                });
            });
        } else if (result.isDenied) { 
            Swal.fire('Purchase not removed', '', 'info');
        } 
    });
}