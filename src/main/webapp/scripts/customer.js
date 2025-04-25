const apiUrl = 'http://localhost:8080/Inventory_System/customer';

/*--------displayData Function------*/
function displayData() {
    fetch(apiUrl)
    .then(response => response.json())
    .then(customers => {
        if (Array.isArray(customers)) {
            var columns = [
                { title: "Customer ID" },
                { title: "Name" },
                { title: "Phone" },
                { title: "Email" },
                { title: "Option" }
            ];
            var data = [];
            var option = '';

            customers.forEach(customer => {
                option = `
                    <input type='button' class='btn btn-info' value='Edit' data-bs-toggle='modal' data-bs-target='#myModal' onclick='editData(${customer.id})'> |
                    <input type='button' class='btn btn-danger' value='Delete' onclick='deleteData(${customer.id})'>
                `;
                data.push([customer.id, customer.name, customer.phone, customer.email, option]);
            });

            console.log(data);

            // Initialize DataTable with the customer data
            $('#table_id').DataTable({
                destroy: true,
                data: data,
                columns: columns
            });
        } else {
            console.error("Expected an array of customers, but got:", customers);
        }
    })
    .catch(error => console.error('Error:', error));
}

/*-------Query Load------*/
$(document).ready(function () {
    displayData(); // Load data when the page is ready
});

/*------Add New Button--------*/
$("#btnadd").click(function() {
    // Clear input fields for new entry
    $("#name").val("");
    $("#phone").val("");
    $("#email").val("");
    $("#btnsave").text("Insert"); // Change the button text to "Insert"
});

/*-------Save Button--------*/
$("#btnsave").click(function() {
    const name = document.getElementById('name').value;
    const phone = document.getElementById('phone').value;
    const email = document.getElementById('email').value;

    // Check if fields are not empty
    if (!name || !phone || !email) {
        Swal.fire({ title: 'All fields are required', icon: 'error' });
        return;
    }

    const customerData = { name, phone, email };
    console.log("Request Body:", JSON.stringify(customerData)); // Debugging: Log request body

    if ($("#btnsave").text() === "Insert") {
        // Insert new customer
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(customerData),
        })
        .then(response => {
            console.log("Response Status:", response.status);  // Log status code
            console.log("Response Headers:", response.headers);  // Log response headers
            return response.json();
        })
        .then(data => {
            Swal.fire({ title: data.message, icon: "success" });
            displayData(); // Refresh the table data
            $('#myModal').modal('hide'); // Close the modal
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({ title: 'Error occurred', icon: 'error' });
        });

    } else {
        // Update existing customer
        fetch(apiUrl, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ id: customer_id, name, phone, email }), // Send the data with ID
        })
        .then(response => response.json())
        .then(data => {
            Swal.fire({ title: data.message, icon: "success" });
            displayData(); // Refresh the table data
            $('#myModal').modal('hide'); // Close the modal
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({ title: 'Error occurred', icon: 'error' });
        });
    }
});

var customer_id; // Global variable to store the customer ID for editing

/*--------Edit Button-------*/
function editData(id) {
    $("#btnsave").text("Update"); // Change button text to "Update"
    customer_id = id; // Store the customer ID to use for the update

    fetch(apiUrl + "?id=" + id)
    .then(response => response.json())
    .then(data => {
        if (data.length > 0) {
            // Assuming the response is an array, where the first object contains the customer details
            document.getElementById("name").value = data[0].name;
            document.getElementById("phone").value = data[0].phone;
            document.getElementById("email").value = data[0].email;
        } else {
            console.error("No data found for customer with id: " + id);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        Swal.fire({ title: 'Error fetching customer details', icon: 'error' });
    });
}

/*------Delete Button--------*/
function deleteData(id) {
    Swal.fire({
        title: 'Do you want to remove?',
        showDenyButton: true,
        confirmButtonText: 'Yes',
        denyButtonText: 'No',
        icon: "question",
    }).then((result) => {
        if (result.isConfirmed) {
            // Delete the customer if confirmed
            fetch(apiUrl, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ id }), // Send the ID in the request body
            })
            .then(response => response.json())
            .then(data=> {
                Swal.fire({ title: data.message, icon: "success" });
                displayData(); // Refresh the table data
            })
            .catch(error => {
                console.error('Error:', error);
                Swal.fire({ title: 'Error deleting customer', icon: 'error' });
            });
        } else if (result.isDenied) {
            Swal.fire('Data are not removed', '', 'info');
        }
    });
}