  // Script for validating an email from form
  function checkEmailUnique(form) {
    const url = contextPath + "customers/check_email";
    const email = $("#email").val();
    const csrfValue = $("input[name='_csrf']").val(); // used 'input' because _csrf field is hidden
    const id = $('#id').val();
    const params = {id: id, email: email, _csrf: csrfValue};

    $.post(url, params, (response) => {
      if (response === "OK") {
        form.submit();
      } else if (response === "Duplicated") {
        showModalDialog("Warning", "There is another user having the email : " + email)
      } else {
        showModalDialog("Error", "Unknown response from server!")
      }
    }).fail(() => {
        showModalDialog("Error", "Could not connect to the server!")
    });
    return false;
}
