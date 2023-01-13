  // Script for validating an email from form
  function checkEmailUnique(form) {
    const url = "/ShopAdmin/api/v1/users/check_email";
    const userEmail = $("#email").val();
    const csrfValue = $("input[name='_csrf']").val(); // used 'input' because _csrf field is hidden
    const userId = $('#id').val();
    const params = {id: userId, email: userEmail, _csrf: csrfValue};

    $.post(url, params, (response) => {
      if (response === "OK") {
        form.submit();
      } else if (response === "Duplicated") {
        showModalDialog("Warning", "There is another user having the email : " + userEmail)
      } else {
        showModalDialog("Error", "Unknown response from server!")
      }
    })
    .fail(() => {
        showModalDialog("Error", "Could not connect to the server!")
    });
    return false;
  }
