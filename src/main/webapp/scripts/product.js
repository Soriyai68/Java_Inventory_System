const apiUrl = 'http://localhost:8080/Inventory_System/product';

/*--------displayData Function------*/
function displayData() {
    fetch(apiUrl)
    .then(response => {
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    })
    .then(products => {
        console.log('Products fetched:', products);
        var data = [];
        var option = '';
        products.forEach(product => {
            option = `
                <button class="btn btn-edit" data-bs-toggle="modal" data-bs-target="#myModal" onclick="editData(${product.id})">
                    <i class="fas fa-pen"></i> Edit
                </button>
                <button class="btn btn-delete" onclick="deleteData(${product.id})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            `;
            data.push([
                product.id,
                product.name,
                product.sku,
                product.brand && product.brand.name ? product.brand.name : 'N/A',
                product.category && product.category.name ? product.category.name : 'N/A',
                product.defaultPrice,
                product.sellingPrice,
                product.imageUrl ? `<img src="${product.imageUrl}" width="50" alt="Product Image">` : 'No Image',
                product.currentStock,
                product.unit && product.unit.name ? product.unit.name : 'N/A',
                product.descriptions ? product.descriptions.substring(0, 50) + (product.descriptions.length > 50 ? '...' : '') : '',
                option
            ]);
        });
        console.log('Data for DataTable:', data);

        $('#table_id').DataTable({
            destroy: true,
            data: data,
            columns: [
                { title: "Product ID" },
                { title: "Name" },
                { title: "SKU" },
                { title: "Brand" },
                { title: "Category" },
                { title: "Default Price" },
                { title: "Selling Price" },
                { title: "Image" },
                { title: "Stock" },
                { title: "Unit" },
                { title: "Descriptions" },
                { title: "Option" }
            ],
            columnDefs: [
                { 
                    targets: 5, // Default Price column (index 5)
                    render: function(data, type, row) {
                        return data ? '$' + parseFloat(data).toFixed(2) : '$0.00';
                    }
                },
                { 
                    targets: 6, // Selling Price column (index 6)
                    render: function(data, type, row) {
                        return data ? '$' + parseFloat(data).toFixed(2) : '$0.00';
                    }
                },
                { targets: 11, className: 'option-column' } // Option column (index 11)
            ]
        });
    })
    .catch(error => {
        console.error('Error in displayData:', error);
        Swal.fire({
            title: 'Error Loading Products',
            text: error.message,
            icon: 'error'
        });
    });
}

/*-------Query Load------*/
$(document).ready(function () {
    displayData();
    loadDropdowns();

    $('#image').change(function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                $('#imagePreview').html(`<img src="${e.target.result}" class="img-thumbnail" style="max-width: 100px;">`);
            };
            reader.readAsDataURL(file);
        } else {
            $('#imagePreview').empty();
        }
    });
});

/*------Load Dropdowns for Unit, Brand, Category------*/
function loadDropdowns() {
    fetch('http://localhost:8080/Inventory_System/unit')
    .then(response => response.json())
    .then(units => {
        let unitSelect = $('#unitId');
        unitSelect.empty();
        unitSelect.append('<option value="">Select Unit</option>');
        units.forEach(unit => {
            unitSelect.append(`<option value="${unit.id}">${unit.name}</option>`);
        });
    })
    .catch(error => console.error('Error loading units:', error));

    fetch('http://localhost:8080/Inventory_System/brand')
    .then(response => response.json())
    .then(brands => {
        let brandSelect = $('#brandId');
        brandSelect.empty();
        brandSelect.append('<option value="">Select Brand</option>');
        brands.forEach(brand => {
            brandSelect.append(`<option value="${brand.id}">${brand.name}</option>`);
        });
    })
    .catch(error => console.error('Error loading brands:', error));

    fetch('http://localhost:8080/Inventory_System/category')
    .then(response => response.json())
    .then(categories => {
        let categorySelect = $('#categoryId');
        categorySelect.empty();
        categorySelect.append('<option value="">Select Category</option>');
        categories.forEach(category => {
            categorySelect.append(`<option value="${category.id}">${category.name}</option>`);
        });
    })
    .catch(error => console.error('Error loading categories:', error));
}

/*------AddNew Button--------*/
$("#btnadd").click(function() {
    $("#id").val("");
    $("#name").val("");
    $("#sku").val("");
    $("#unitId").val("");
    $("#brandId").val("");
    $("#categoryId").val("");
    $("#defaultPrice").val("");
    $("#sellingPrice").val("");
    $("#image").val("");
    $("#imagePreview").empty();
    $("#currentStock").val("0"); // Set to 0 explicitly
    $("#descriptions").val("");
    $("#btnsave").text("Insert");
});

/*-------Save Button--------*/
$("#btnsave").click(function() {
    const formData = new FormData();
    formData.append('name', document.getElementById('name').value);
    formData.append('sku', document.getElementById('sku').value);
    formData.append('unitId', document.getElementById('unitId').value);
    formData.append('brandId', document.getElementById('brandId').value);
    formData.append('categoryId', document.getElementById('categoryId').value);
    formData.append('defaultPrice', document.getElementById('defaultPrice').value);
    formData.append('sellingPrice', document.getElementById('sellingPrice').value);
    const imageFile = document.getElementById('image').files[0];
    if (imageFile) {
        formData.append('image', imageFile);
    }
    formData.append('currentStock', '0'); // Always send 0 for currentStock
    formData.append('descriptions', document.getElementById('descriptions').value);

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
        formData.append('id', product_id);
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

var product_id;
/*--------edit button-------*/
function editData(id) {
    $("#btnsave").text("Update");
    product_id = id;
    fetch(apiUrl + "?id=" + id)
    .then(response => response.json())
    .then(data => {
        document.getElementById("id").value = data.id;
        document.getElementById("name").value = data.name;
        document.getElementById("sku").value = data.sku;
        document.getElementById("unitId").value = data.unit.id;
        document.getElementById("brandId").value = data.brand.id;
        document.getElementById("categoryId").value = data.category.id;
        document.getElementById("defaultPrice").value = data.defaultPrice;
        document.getElementById("sellingPrice").value = data.sellingPrice;
        if (data.imageUrl) {
            $('#imagePreview').html(`<img src="${data.imageUrl}" class="img-thumbnail" style="max-width: 100px;">`);
        } else {
            $('#imagePreview').empty();
        }
        document.getElementById("currentStock").value = data.currentStock; // Show actual stock but keep readonly
        document.getElementById("descriptions").value = data.descriptions;
    })
    .catch(error => console.error('Error:', error));
}

/*------delete button--------*/
function deleteData(id) {
    Swal.fire({ 
        title: 'Do you want to remove this product?', 
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
                    title: 'Failed to delete product',
                    text: error.message,
                    icon: 'error'
                });
            });
        } else if (result.isDenied) { 
            Swal.fire('Product not removed', '', 'info');
        } 
    });
}