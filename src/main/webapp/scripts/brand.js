const apiUrl = 'http://localhost:8080/Inventory_System/brand';

/*--------displayData Function------*/
function displayData() {
    fetch(apiUrl)
    .then(response => response.json())
    .then(brands => {
        var columns = [{ title: "Brandid" }, { title: "name" }, { title: "description" }, { title: "option" }];
        var data = [];
        var option = '';
        brands.forEach(brand => {
            option = "<input type='button' class='btn btn-info' value='Edit' data-bs-toggle='modal' data-bs-target='#myModal' onclick='editData(" + brand.id + ")'> | <input type='button' class='btn btn-danger' value='Delete' onclick='deleteData(" + brand.id + ")'>";
            data.push([brand.id, brand.name, brand.description, option]);
        });
        console.log(data);
        $('#table_id').DataTable({
            destroy: true,
            data: data,
            columns: columns
        });
    })
    .catch(error => console.error('Error:', error));
}

/*-------Query Load------*/
$(document).ready(function () {
    displayData();
});

/*------AddNew Button--------*/
$("#btnadd").click(function() {
    $("#name").val("");
    $("#description").val("");
    $("#btnsave").text("Insert");
});

/*-------Save Button--------*/
$("#btnsave").click(function() {
    const name = document.getElementById('name').value;
    const description = document.getElementById('description').value;
    if($("#btnsave").text() == "Insert") {
        // Insert
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, description }),
        })
        .then(response => response.json())
        .then(data => {
            Swal.fire({ title: data.message, icon: "success" });
            displayData();
            $('#myModal').modal('hide');
        })
        .catch(error => console.error('Error:', error));
    } else {
        // Update
        fetch(apiUrl, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ brand_id, name, description }),
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

var brand_id;
/*--------edit button-------*/
function editData(id) {
    $("#btnsave").text("Update");
    brand_id = id;
    fetch(apiUrl + "?id=" + id)
    .then(response => response.json())
    .then(data => {
        document.getElementById("name").value = data[0].name;
        document.getElementById("description").value = data[0].description;
    })
    .catch(error => console.error('Error:', error));
}

/*------delete button--------*/
function deleteData(id) {
    Swal.fire({ 
        title: 'Do you want to remove?', 
        showDenyButton: true, 
        confirmButtonText: 'Yes', 
        denyButtonText: 'No', 
        icon: "question", 
    }).then((result) => { 
        if (result.isConfirmed) { 
            fetch(apiUrl, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ id }),
            })
            .then(response => response.json())
            .then(data => {
                Swal.fire({ title: data.message, icon: "success" });
                displayData();
            })
            .catch(error => console.error('Error:', error));
        } else if (result.isDenied) { 
            Swal.fire('Data are not removed', '', 'info');
        } 
    });
}