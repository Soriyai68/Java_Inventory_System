const apiUrl = 'http://localhost:8080/Inventory_System/unit';

/*--------displayData Function------*/
function displayData() {
    fetch(apiUrl)
    .then(response => response.json())
    .then(units => {
        var columns = [{ title: "unitid" }, { title: "name" }, { title: "subname" }, { title: "description" }, { title: "option" }];
        var data = [];
        var option = '';
        units.forEach(unit => {
            option = "<input type='button' class='btn btn-info' value='Edit' data-bs-toggle='modal' data-bs-target='#myModal' onclick='editData(" + unit.id + ")'> | <input type='button' class='btn btn-danger' value='Delete' onclick='deleteData(" + unit.id + ")'>";
            data.push([unit.id, unit.name, unit.subName, unit.description, option]);
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
    $("#sub_name").val("");
    $("#description").val("");
    $("#btnsave").text("Insert");
});

/*-------Save Button--------*/
$("#btnsave").click(function() {
    const name = document.getElementById('name').value;
    const subName = document.getElementById('sub_name').value;
    const description = document.getElementById('description').value;
    if($("#btnsave").text() == "Insert") {
        // Insert
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ name, sub_name: subName, description }),
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
            body: JSON.stringify({ unit_id, name, sub_name: subName, description }),
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

var unit_id;
/*--------edit button-------*/
function editData(id) {
    $("#btnsave").text("Update");
    unit_id = id;
    fetch(apiUrl + "?id=" + id)
    .then(response => response.json())
    .then(data => {
        document.getElementById("name").value = data[0].name;
        document.getElementById("sub_name").value = data[0].subName;
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
