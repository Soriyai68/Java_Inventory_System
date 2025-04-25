const apiUrl = 'http://localhost:8080/Inventory_System/category';

/*--------displayData Function------*/
function displayData() {
    fetch(apiUrl)
    .then(response => response.json())
    .then(categories => {
        var columns = [{ title: "Category ID" }, { title: "Name" }, { title: "Code" }, { title: "Description" }, { title: "Option" }];
        var data = [];
        var option = '';
        categories.forEach(category => {
            option = "<input type='button' class='btn btn-info' value='Edit' data-bs-toggle='modal' data-bs-target='#myModal' onclick='editData(" + category.id + ")'> | <input type='button' class='btn btn-danger' value='Delete' onclick='deleteData(" + category.id + ")'>";
            data.push([category.id, category.name, category.code, category.description, option]);
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

/*------Add New Button--------*/
$("#btnadd").click(function() {
    $("#name").val("");
    $("#code").val("");
    $("#description").val("");
    $("#btnsave").text("Insert");
});

/*-------Save Button--------*/
$("#btnsave").click(function() {
    const name = document.getElementById('name').value;
    const code = document.getElementById('code').value;
    const description = document.getElementById('description').value;

    if($("#btnsave").text() == "Insert") {
        // Insert
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, code, description }),
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
            body: JSON.stringify({ category_id: category_id, name, code, description }),
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

var category_id;
/*--------edit button-------*/
function editData(id) {
    $("#btnsave").text("Update");
    category_id = id;
    fetch(apiUrl + "?id=" + id)
    .then(response => response.json())
    .then(data => {
        document.getElementById("name").value = data[0].name;
        document.getElementById("code").value = data[0].code;
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
            Swal.fire('Data not removed', '', 'info');
        } 
    });
}
