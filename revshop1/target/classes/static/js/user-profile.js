$(document).ready(function () {
    // Initially show Profile Information by default
    $('#profile-info').show();

    // Sidebar link click event
    $('.nav-link').click(function (e) {
        e.preventDefault();

        // Remove active class from all links
        $('.nav-link').removeClass('active');

        // Add active class to the clicked link
        $(this).addClass('active');

        // Hide all tab-content sections
        $('.tab-content').hide();

        // Get the ID of the clicked tab's related content
        const selectedTab = $(this).attr('id').split('-tab')[0];

        // Show the corresponding content section
        $('#' + selectedTab).show();
    });

    // Trigger click for the first tab to display it on load
    $('#profile-info-tab').trigger('click');
});

// Function to handle profile edit and save for Name, Email, and Mobile Number

function saveProfileField(fieldName, newValue) {
    // Create the payload to send only the updated field
    let payload = {};
    payload[fieldName] = newValue;

    // AJAX request to save profile info
    $.ajax({
        url: '/api/update-profile',  // The same endpoint for profile update
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(payload),
        success: function(response) {
            if(response.status === 'success') {
                alert(`${fieldName} saved successfully!`);
            } else {
                alert(response.message || `Failed to save ${fieldName}. Please try again.`);
            }
        },
        error: function() {
            alert(`Failed to save ${fieldName}. Please try again.`);
        }
    });
}

// First Name and Last Name Edit/Save Functionality
document.getElementById('edit-name').addEventListener('click', function() {
    document.getElementById('first-name').disabled = false;
    document.getElementById('last-name').disabled = false;

    document.getElementById('save-name').classList.remove('d-none');
    this.classList.add('d-none');  // Hide the edit button
});

document.getElementById('save-name').addEventListener('click', function() {
    const firstName = document.getElementById('first-name').value;
    const lastName = document.getElementById('last-name').value;

    // Save first and last names separately
    saveProfileField('firstName', firstName);
    saveProfileField('lastName', lastName);

    // Disable the fields again after saving
    document.getElementById('first-name').disabled = true;
    document.getElementById('last-name').disabled = true;

    document.getElementById('edit-name').classList.remove('d-none'); // Show the edit button again
    this.classList.add('d-none');  // Hide the save button
});

// Email Edit/Save Functionality
document.getElementById('edit-email').addEventListener('click', function() {
    document.getElementById('email').disabled = false;
    document.getElementById('save-email').classList.remove('d-none');
    this.disabled = true;
});

document.getElementById('save-email').addEventListener('click', function() {
    const email = document.getElementById('email').value;
    saveProfileField('email', email);

    document.getElementById('email').disabled = true;
    document.getElementById('save-email').classList.add('d-none');
    document.getElementById('edit-email').disabled = false;
});

// Mobile Number Edit/Save Functionality
document.getElementById('edit-mobile').addEventListener('click', function() {
    document.getElementById('mobile').disabled = false;
    document.getElementById('save-mobile').classList.remove('d-none');
    this.disabled = true;
});

document.getElementById('save-mobile').addEventListener('click', function() {
    const mobile = document.getElementById('mobile').value;
    saveProfileField('phoneNumber', mobile);

    document.getElementById('mobile').disabled = true;
    document.getElementById('save-mobile').classList.add('d-none');
    document.getElementById('edit-mobile').disabled = false;
});

// Gender Edit/Save Functionality
document.getElementById('edit-gender').addEventListener('click', function () {
    // Enable the gender radio buttons for editing
    document.getElementById('gender-male').disabled = false;
    document.getElementById('gender-female').disabled = false;

    document.getElementById('save-gender').classList.remove('d-none');
    this.classList.add('d-none');
});

document.getElementById('save-gender').addEventListener('click', function () {
    const selectedGender = document.querySelector('input[name="gender"]:checked').value;

    // Send the selected gender to the backend
    saveProfileField('gender', selectedGender);

    // Disable the gender radio buttons after saving
    document.getElementById('gender-male').disabled = true;
    document.getElementById('gender-female').disabled = true;

    document.getElementById('edit-gender').classList.remove('d-none');
    this.classList.add('d-none');  // Hide the save button
});

// Delete Account functionality
document.querySelector('.btn-danger').addEventListener('click', function () {
    if (confirm("Are you sure you want to delete your account? This action is irreversible.")) {
        // Send delete request to the backend
        $.ajax({
            url: '/api/delete-account',
            type: 'DELETE',
            success: function (response) {
                if (response.status === 'success') {
                    alert('Account deleted successfully. Redirecting to homepage.');
                    window.location.href = '/';
                } else {
                    alert('Failed to delete account. Please try again.');
                }
            },
            error: function () {
                alert('Failed to delete account. Please try again.');
            }
        });
    }
});



// Handle form submission for saving the address
document.getElementById('address-form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent form from submitting normally

    // Gather input values
    const name = document.getElementById('address-name').value;
    const mobile = document.getElementById('address-mobile').value;
    const pincode = document.getElementById('pincode').value;
    const locality = document.getElementById('locality').value;
    const address = document.getElementById('address').value;
    const city = document.getElementById('city').value;
    const state = document.getElementById('state').value;
    const landmark = document.getElementById('landmark').value;
    const addressType = document.querySelector('input[name="addressType"]:checked').value;

    // Create the payload to send
    const addressData = {
        fullName: name,
        phoneNumber: mobile,
        address: address,
        locality: locality,
        pincode: pincode,
        city: city,
        state: state,
        landmark: landmark,
        addressType: addressType
    };

    // Make the AJAX call to save the address
    $.ajax({
        url: '/profile/update-address',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(addressData),
		
        success: function(response) {
            if (response.status === 'success') {
                // Update the display with the saved address
                document.getElementById('address-details').innerHTML = `
                    <p><strong>Name:</strong> ${name}</p>
                    <p><strong>Mobile:</strong> ${mobile}</p>
                    <p><strong>Address:</strong> ${locality}, ${address}, ${city}, ${state} - ${pincode}</p>
                    <p><strong>Landmark:</strong> ${landmark || 'N/A'}</p>
                    <p><strong>Address Type:</strong> ${addressType.charAt(0).toUpperCase() + addressType.slice(1)}</p>
                `;

                // Hide the form and show the saved address section
                document.getElementById('manage-address').style.display = 'none';
                document.getElementById('saved-address').style.display = 'block';
                alert('Address saved successfully!');
            } else {
                alert('Failed to save address: ' + response.message);
            }
        },
        error: function() {
            alert('An error occurred while saving the address. Please try again.');
        }
    });
});

// Handle "Edit" button click for address
document.getElementById('edit-address-btn').addEventListener('click', function() {
    // Hide the saved address and show the form again
    document.getElementById('saved-address').style.display = 'none';
    document.getElementById('manage-address').style.display = 'block';
});
