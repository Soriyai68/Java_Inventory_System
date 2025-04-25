const apiUrl = 'http://localhost:8080/Inventory_System/adjuststock';
const productApiUrl = 'http://localhost:8080/Inventory_System/product';

function displayData() {
    fetch(apiUrl, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}, url: ${apiUrl}`);
            return response.json();
        })
        .then(adjustStocks => {
            const data = adjustStocks.map(adjustStock => [
                adjustStock.id || 'N/A',
                adjustStock.product.name || 'N/A',
                adjustStock.option || 'N/A',
                adjustStock.date || 'N/A',
                adjustStock.adjustStock || 0,
                adjustStock.price ? '$' + parseFloat(adjustStock.price).toFixed(2) : '$0.00',
                adjustStock.totalPrice ? '$' + parseFloat(adjustStock.totalPrice).toFixed(2) : '$0.00',
                adjustStock.description ? adjustStock.description.substring(0, 50) + (adjustStock.description.length > 50 ? '...' : '') : '',
                adjustStock.product.currentStock !== null ? adjustStock.product.currentStock : 'N/A',
                `<button class="btn btn-edit" data-bs-toggle="modal" data-bs-target="#myModal" onclick="editData(${adjustStock.id})">
                    <i class="fas fa-pen"></i> Edit
                 </button>
                 <button class="btn btn-delete" onclick="deleteData(${adjustStock.id})">
                    <i class="fas fa-trash"></i> Delete
                 </button>`
            ]);

            $('#table_id').DataTable({
                destroy: true,
                data: data,
                columns: [
                    { title: "Adjustment ID" },
                    { title: "Product" },
                    { title: "Type" },
                    { title: "Date" },
                    { title: "Quantity Adjusted" },
                    { title: "Unit Price" },
                    { title: "Total Price" },
                    { title: "Description" },
                    { title: "Current Stock" },
                    { title: "Option" }
                ],
                columnDefs: [{ targets: 9, className: 'option-column' }]
            });
        })
        .catch(error => {
            console.error('Error in displayData:', error);
            Swal.fire({ 
                title: 'Error Loading Adjustments', 
                text: error.message.includes('Failed to fetch') ? 'Cannot connect to server at ' + apiUrl + '. Please check if it is running.' : error.message, 
                icon: 'error' 
            });
        });
}

function loadDropdowns() {
    fetch(productApiUrl, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}, url: ${productApiUrl}`);
            return response.json();
        })
        .then(products => {
            const productSelect = $('#product');
            productSelect.empty().append('<option value="">Select Product</option>');
            products.forEach(product => {
                productSelect.append(
                    `<option value="${product.id}" data-price="${product.sellingPrice}" data-current-stock="${product.currentStock}">${product.name} ($${parseFloat(product.sellingPrice).toFixed(2)})</option>`
                );
            });
        })
        .catch(error => {
            console.error('Error loading products:', error);
            Swal.fire({ 
                title: 'Error Loading Products', 
                text: error.message.includes('Failed to fetch') ? 'Cannot connect to server at ' + productApiUrl + '. Please check if it is running.' : error.message, 
                icon: 'error' 
            });
        });
}

function updatePriceAndTotal() {
    const adjustStock = parseFloat($('#adjust_stock').val()) || 0;
    const price = parseFloat($('#product option:selected').data('price')) || 0;
    $('#price').val(price.toFixed(2));
    $('#total_price').val((price * adjustStock).toFixed(2));
}

$(document).ready(function () {
    displayData();
    loadDropdowns();
    $('#product').on('change', updatePriceAndTotal);
    $('#adjust_stock').on('input', updatePriceAndTotal);

    $('#adjustStockForm').on('submit', function(e) {
        e.preventDefault();
        const productId = $('#product').val();
        const adjustStock = parseFloat($('#adjust_stock').val()) || 0;
        const date = $('#date').val();
        $('#product, #adjust_stock, #date').removeClass('is-invalid');
        
        if (!productId) {
            $('#product').addClass('is-invalid');
            Swal.fire({ title: 'Invalid Input', text: 'Please select a product', icon: 'error' });
            return;
        }
        if (adjustStock === 0) {
            $('#adjust_stock').addClass('is-invalid');
            Swal.fire({ title: 'Invalid Input', text: 'Quantity adjusted cannot be zero', icon: 'error' });
            return;
        }
        if (!date) {
            $('#date').addClass('is-invalid');
            Swal.fire({ title: 'Invalid Input', text: 'Please select a date', icon: 'error' });
            return;
        }
        $('#btnsave').click();
    });
});

$("#btnadd").click(function() {
    $("#product").val("");
    $("#option").val("");
    $("#date").val("");
    $("#adjust_stock").val("");
    $("#price").val("");
    $("#total_price").val("");
    $("#description").val("");
    $("#btnsave").text("Insert");
    $('#adjustStockForm').find(':input').removeClass('is-invalid');
});

let adjust_id = null;
function editData(id) {
    if (!id) {
        Swal.fire({ title: 'Error', text: 'Invalid adjustment ID', icon: 'error' });
        return;
    }
    $("#btnsave").text("Update");
    adjust_id = id;
    fetch(`${apiUrl}?id=${id}`, { method: 'GET' })
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}, url: ${apiUrl}?id=${id}`);
            return response.json();
        })
        .then(data => {
            $('#product').val(data.product.id || '');
            $('#option').val(data.option || '');
            $('#date').val(data.date || '');
            $('#adjust_stock').val(data.adjustStock || '');
            $('#price').val(data.price ? parseFloat(data.price).toFixed(2) : '');
            $('#total_price').val(data.totalPrice ? parseFloat(data.totalPrice).toFixed(2) : '');
            $('#description').val(data.description || '');
            $('#adjustStockForm').find(':input').removeClass('is-invalid');
        })
        .catch(error => {
            console.error('Error in editData:', error);
            Swal.fire({ 
                title: 'Error Loading Adjustment', 
                text: error.message.includes('Failed to fetch') ? 'Cannot connect to server at ' + apiUrl + '. Please check if it is running.' : error.message, 
                icon: 'error' 
            });
        });
}

$("#btnsave").click(function() {
    const formData = new FormData();
    const productId = $('#product').val();
    const option = $('#option').val();
    const date = $('#date').val();
    const adjustStock = $('#adjust_stock').val();
    const price = $('#price').val();
    const totalPrice = $('#total_price').val();
    const description = $('#description').val();

    formData.append('product_id', productId);
    formData.append('option', option);
    formData.append('date', date);
    formData.append('adjust_stock', adjustStock);
    formData.append('price', price);
    formData.append('total_price', totalPrice);
    formData.append('description', description);

    const method = $("#btnsave").text() === "Insert" ? 'POST' : 'PUT';
    const url = method === 'PUT' ? `${apiUrl}?id=${adjust_id}` : apiUrl;

    console.log('Saving adjustment:', {
        method,
        url,
        productId,
        adjustStock,
        date,
        option,
        price,
        totalPrice,
        description
    });
    for (let [key, value] of formData.entries()) {
        console.log(`FormData: ${key} = ${value}`);
    }

    fetch(url, {
        method: method,
        body: formData,
        headers: {
            'Accept': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            return response.text().then(text => {
                throw new Error(`HTTP error! status: ${response.status}, body: ${text}, url: ${url}`);
            });
        }
        return response.json();
    })
    .then(data => {
        Swal.fire({ title: data.message, icon: 'success' });
        displayData();
        $('#myModal').modal('hide');
        $('#adjustStockForm').find(':input').removeClass('is-invalid');
    })
    .catch(error => {
        console.error('Error in save:', error);
        let errorMessage = error.message;
        if (error.message.includes('Failed to fetch')) {
            errorMessage = 'Unable to connect to the server at ' + url + '. Please ensure it is running.';
        }
        Swal.fire({ title: 'Error Saving Adjustment', text: errorMessage, icon: 'error' });
    });
});

function deleteData(id) {
    Swal.fire({ 
        title: 'Do you want to remove this adjustment?', 
        showDenyButton: true, 
        confirmButtonText: 'Yes', 
        denyButtonText: 'No', 
        icon: 'question'
    }).then((result) => { 
        if (result.isConfirmed) { 
            fetch(`${apiUrl}?id=${id}`, { method: 'DELETE' })
                .then(response => {
                    if (!response.ok) throw new Error(`HTTP error! status: ${response.status}, url: ${apiUrl}?id=${id}`);
                    return response.json();
                })
                .then(data => {
                    Swal.fire({ title: data.message, icon: 'success' });
                    displayData();
                })
                .catch(error => {
                    console.error('Error in delete:', error);
                    Swal.fire({ 
                        title: 'Failed to Delete Adjustment', 
                        text: error.message.includes('Failed to fetch') ? 'Cannot connect to server at ' + apiUrl + '. Please check if it is running.' : error.message, 
                        icon: 'error' 
                    });
                });
        } else if (result.isDenied) { 
            Swal.fire('Adjustment not removed', '', 'info');
        } 
    });
}