function checkName(form) {
  const url = moduleURL + "/check_name";
  const name = $("#name").val();
  const id = $("#id").val();
  const csrf = $("input[name='_csrf']").val();
  const params = { id: id, name: name, _csrf: csrf };

  $.post(url, params, (response) => {
    if (response === "OK") {
      form.submit();
    } else if (response !== "OK") {
      showModalDialog(
        "Warning",
        `There is another ${response} having the name:  ${name}`
      );
    } else {
      showModalDialog("Error", "Unknown response from server!");
    }
  }).fail(() => {
    showModalDialog("Error", "Could not connect to the server!");
  });
  return false;
}
