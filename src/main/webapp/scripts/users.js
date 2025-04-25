// src/main/webapp/js/users.js
const apiUrl = 'http://localhost:8080/Inventory_System/users';
const loginApiUrl = 'http://localhost:8080/Inventory_System/login';
const logoutApiUrl = 'http://localhost:8080/Inventory_System/logout';

/*--------login Function------*/
function login() {
    const pin = document.getElementById('pin').value.trim();
    if (!pin) {
        Swal.fire({ title: 'PIN is required', icon: 'warning' });
        return;
    }
    if (!/^\d{4,6}$/.test(pin)) {
        Swal.fire({ title: 'PIN must be 4-6 digits', icon: 'warning' });
        return;
    }
    const payload = { pin: pin };
    console.log('Sending login request:', JSON.stringify(payload));
    fetch(loginApiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
        credentials: 'include'
    })
    .then(response => {
        console.log('Login response status:', response.status);
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(err.error || `HTTP ${response.status}`);
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Login response data:', data);
        if (data.error) {
            Swal.fire({ title: data.error, icon: 'error' });
            return;
        }
        Swal.fire({ title: data.message, icon: 'success' });
        localStorage.setItem('role', data.role);
        window.location.href = '/Inventory_System/layout';
    })
    .catch(error => {
        Swal.fire({ title: 'Login Failed', text: error.message, icon: 'error' });
        console.error('Login error:', error);
    });
}

/*--------logout Function------*/
function logout() {
    fetch(logoutApiUrl, {
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        Swal.fire({ title: data.message, icon: 'success' });
        localStorage.removeItem('role');
        window.location.href = '/Inventory_System/login.jsp';
    })
    .catch(error => {
        Swal.fire({ title: 'Logout Failed', icon: 'error' });
        console.error('Logout error:', error);
    });
}

/*--------displayData Function------*/
function displayData() {
    console.log('Fetching users from:', apiUrl);
    fetch(apiUrl, {
        headers: {
            'Content-Type': 'application/json',
        },
        credentials: 'include'
    })
    .then(response => {
        console.log('Users fetch response status:', response.status);
        response.clone().text().then(raw => {
            console.log('Raw response:', raw);
        });
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(`${err.error || 'Failed to fetch users'} (HTTP ${response.status})`);
            }).catch(() => {
                throw new Error(`Failed to fetch users (HTTP ${response.status})`);
            });
        }
        return response.json();
    })
    .then(users => {
        console.log('Fetched users:', users);
        const columns = [
            { title: "User ID" },
            { title: "PIN" },
            { title: "Role" },
            { title: "Created At" },
            { title: "Option" }
        ];
        const data = users.map(user => [
            user.id,
            user.pin,
            user.role,
            user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A',
            `<button class='btn btn-info' data-bs-toggle='modal' data-bs-target='#userModal' onclick='editData(${user.id})'>Edit</button> | 
             <button class='btn btn-danger' onclick='deleteData(${user.id})'>Delete</button>`
        ]);
		$('#table_id').DataTable({
		    destroy: true,
		    data: data,
		    columns: columns,
		    pageLength: 10,
		    responsive: true
		});
    })
    .catch(error => {
        Swal.fire({ title: 'Failed to load users', text: error.message, icon: 'error' });
        console.error('Error fetching users:', error);
    });
}

/*-------Query Load------*/
$(document).ready(function () {
    if (window.location.pathname.includes('layout') && window.location.search.includes('action=users')) {
        console.log('Initializing DataTable for users.jsp');
        displayData();
    }

    /*------AddNew Button--------*/
    $("#btnadd").click(function() {
        $("#pin").val("");
        $("#role").val("user");
        $("#btnsave").text("Insert");
        $('#userModal').modal('show');
    });

    /*-------Save Button--------*/
    $("#btnsave").click(function() {
        const pin = document.getElementById('pin').value.trim();
        const role = document.getElementById('role').value.trim();
        if (!pin || !role) {
            Swal.fire({ title: 'PIN and role are required', icon: 'warning' });
            return;
        }
        if (!/^\d{4,6}$/.test(pin)) {
            Swal.fire({ title: 'PIN must be 4-6 digits', icon: 'warning' });
            return;
        }
        if (!['admin', 'user'].includes(role.toLowerCase())) {
            Swal.fire({ title: 'Role must be admin or user', icon: 'warning' });
            return;
        }

        const payload = { pin, role };
        if ($("#btnsave").text() === "Insert") {
            fetch(apiUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(payload)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.error || 'Failed to create user');
                    });
                }
                return response.json();
            })
            .then(data => {
                Swal.fire({ title: data.message || 'User created', icon: 'success' });
                displayData();
                $('#userModal').modal('hide');
            })
            .catch(error => {
                Swal.fire({ title: 'Failed to create user', text: error.message, icon: 'error' });
                console.error('Create user error:', error);
            });
        } else {
            payload.id = user_id;
            fetch(apiUrl, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(payload)
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.error || 'Failed to update user');
                    });
                }
                return response.json();
            })
            .then(data => {
                Swal.fire({ title: data.message || 'User updated', icon: 'success' });
                displayData();
                $('#userModal').modal('hide');
            })
            .catch(error => {
                Swal.fire({ title: 'Failed to update user', text: error.message, icon: 'error' });
                console.error('Update user error:', error);
            });
        }
    });

    /*-------Login Form--------*/
    $("#loginForm").on('submit', function(e) {
        e.preventDefault();
        login();
    });
});

var user_id;
/*--------edit button-------*/
function editData(id) {
    $("#btnsave").text("Update");
    user_id = id;
    fetch(apiUrl + "?id=" + id, {
        credentials: 'include'
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => {
                throw new Error(err.error || 'Failed to fetch user');
            });
        }
        return response.json();
    })
    .then(data => {
        console.log('Fetched user for edit:', data);
        document.getElementById("pin").value = data.pin;
        document.getElementById("role").value = data.role;
        $('#userModal').modal('show');
    })
    .catch(error => {
        Swal.fire({ title: 'Failed to load user', text: error.message, icon: 'error' });
        console.error('Edit user error:', error);
    });
}

/*------delete button--------*/
function deleteData(id) {
    Swal.fire({
        title: 'Do you want to remove this user?',
        showDenyButton: true,
        confirmButtonText: 'Yes',
        denyButtonText: 'No',
        icon: 'question'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(apiUrl, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({ id })
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.error || 'Failed to delete user');
                    });
                }
                return response.json();
            })
            .then(data => {
                Swal.fire({ title: 'User deleted', icon: 'success' });
                displayData();
            })
            .catch(error => {
                Swal.fire({ title: 'Failed to delete user', text: error.message, icon: 'error' });
                console.error('Delete user error:', error);
            });
        } else if (result.isDenied) {
            Swal.fire('User not removed', '', 'info');
        }
    });
}