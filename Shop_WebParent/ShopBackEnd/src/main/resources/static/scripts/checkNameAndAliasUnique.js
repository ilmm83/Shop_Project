function checkNameAndAliasUnique(form) {
  const url = moduleURL + "/check_name_and_alias";
  const name = $("#name").val();
  let alias = $("#alias").val();
  const id = $("#id").val();
  const csrf = $("input[name='_csrf']").val();
  
  if (alias == undefined || alias == null || alias.length == 0) {
    alias = name;
    alias.replace(' ', '-');
  }

  const params = { id: id, name: name, alias: alias, _csrf: csrf };
  
  $.post(url, params, (response) => {
    if (response === "OK") {
      form.submit();
    } else if (response === "Alias") {
      showModalDialog(
        "Warning",
        "There is another category having the alias: " + alias
      );
    } else if (response === "Name") {
      showModalDialog(
        "Warning",
        "There is another category having the name: " + name
      );
    } else {
      showModalDialog("Error", "Unknown response from server!");
    }
  }).fail(() => {
    showModalDialog("Error", "Could not connect to the server!");
  });
  return false;
}
