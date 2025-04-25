const apiUrl = 'http://localhost:8080/Inventory_System/sell';
const productApiUrl = 'http://localhost:8080/Inventory_System/product';
const customerApiUrl = 'http://localhost:8080/Inventory_System/customer';

function displayData() {
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            return response.json();
        })
        .then(sells => {
            const data = sells.map(sell => [
                sell.id || 'N/A',
                sell.product.name || 'N/A',
                sell.customer.name || 'N/A',
                sell.date || 'N/A',
                sell.saleStock || 0,
                sell.price ? '$' + parseFloat(sell.price).toFixed(2) : '$0.00',
                sell.totalPrice ? '$' + parseFloat(sell.totalPrice).toFixed(2) : '$0.00',
                sell.description ? sell.description.substring(0, 50) + (sell.description.length > 50 ? '...' : '') : '',
                `<button class="btn btn-edit" data-bs-toggle="modal" data-bs-target="#myModal" onclick="editData(${sell.id})">
                    <i class="fas fa-pen"></i> Edit
                 </button>
                 <button class="btn btn-delete" onclick="deleteData(${sell.id})">
                    <i class="fas fa-trash"></i> Delete
                 </button>`
            ]);

            $('#table_id').DataTable({
                destroy: true,
                data: data,
                columns: [
                    { title: "Sale ID" },
                    { title: "Product" },
                    { title: "Customer" },
                    { title: "Date" },
                    { title: "Quantity Sold" },
                    { title: "Unit Price" },
                    { title: "Total Price" },
                    { title: "Description" },
                    { title: "Option" }
                ],
                columnDefs: [{ targets: 8, className: 'option-column' }]
            });
        })
        .catch(error => {
            console.error('Error in displayData:', error);
            Swal.fire({ title: 'Error Loading Sales', text: error.message, icon: 'error' });
        });
}

function loadDropdowns() {
    fetch(productApiUrl)
        .then(response => response.json())
        .then(products => {
            const productSelect = $('#product');
            productSelect.empty().append('<option value="">Select Product</option>');
            products.forEach(product => {
                productSelect.append(
                    `<option value="${product.id}" data-price="${product.sellingPrice}">${product.name} ($${parseFloat(product.sellingPrice).toFixed(2)})</option>`
                );
            });
        })
        .catch(error => console.error('Error loading products:', error));

    fetch(customerApiUrl)
        .then(response => response.json())
        .then(customers => {
            const customerSelect = $('#customer');
            customerSelect.empty().append('<option value="">Select Customer</option>');
            customers.forEach(customer => {
                customerSelect.append(`<option value="${customer.id}">${customer.name}</option>`);
            });
        })
        .catch(error => console.error('Error loading customers:', error));
}

function updatePriceAndTotal() {
    const saleStock = parseFloat($('#sale_stock').val()) || 0;
    const price = parseFloat($('#product option:selected').data('price')) || 0;
    $('#price').val(price.toFixed(2));
    $('#total_price').val((price * saleStock).toFixed(2));
}

$(document).ready(function () {
    displayData();
    loadDropdowns();
    $('#product').on('change', updatePriceAndTotal);
    $('#sale_stock').on('input', updatePriceAndTotal);
});

$("#btnadd").click(function() {
    $("#product").val("");
    $("#customer").val("");
    $("#date").val("");
    $("#sale_stock").val("");
    $("#price").val("");
    $("#total_price").val("");
    $("#description").val("");
    $("#btnsave").text("Insert");
});

let sale_id;
function editData(id) {
    $("#btnsave").text("Update");
    sale_id = id;
    fetch(`${apiUrl}?id=${id}`)
        .then(response => {
            if (!response.ok) throw new Error('Sale not found');
            return response.json();
        })
        .then(data => {
            $('#product').val(data.product.id);
            $('#customer').val(data.customer.id);
            $('#date').val(data.date);
            $('#sale_stock').val(data.saleStock);
            $('#price').val(parseFloat(data.price).toFixed(2));
            $('#total_price').val(parseFloat(data.totalPrice).toFixed(2));
            $('#description').val(data.description || '');
        })
        .catch(error => console.error('Error in editData:', error));
}

$("#btnsave").click(function() {
    const formData = new FormData();
    formData.append('product_id', $('#product').val());
    formData.append('customer_id', $('#customer').val());
    formData.append('date', $('#date').val());
    formData.append('sale_stock', $('#sale_stock').val());
    formData.append('price', $('#price').val());
    formData.append('total_price', $('#total_price').val());
    formData.append('description', $('#description').val());

    const method = $("#btnsave").text() === "Insert" ? 'POST' : 'PUT';
    const url = method === 'PUT' ? `${apiUrl}?id=${sale_id}` : apiUrl;

    fetch(url, {
        method: method,
        body: formData
    })
    .then(response => {
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    })
    .then(data => {
        Swal.fire({ title: data.message, icon: "success" });
        displayData();
        $('#myModal').modal('hide');
    })
    .catch(error => {
        console.error('Error in save:', error);
        Swal.fire({ title: 'Error saving sale', text: error.message, icon: "error" });
    });
});

function deleteData(id) {
    Swal.fire({ 
        title: 'Do you want to remove this sale?', 
        showDenyButton: true, 
        confirmButtonText: 'Yes', 
        denyButtonText: 'No', 
        icon: "question"
    }).then((result) => { 
        if (result.isConfirmed) { 
            fetch(`${apiUrl}?id=${id}`, { method: 'DELETE' })
                .then(response => {
                    if (!response.ok) throw new Error('Deletion failed');
                    return response.json();
                })
                .then(data => {
                    Swal.fire({ title: data.message, icon: "success" });
                    displayData();
                })
                .catch(error => {
                    console.error('Error in delete:', error);
                    Swal.fire({ title: 'Failed to delete sale', text: error.message, icon: 'error' });
                });
        } else if (result.isDenied) { 
            Swal.fire('Sale not removed', '', 'info');
        } 
    });
}