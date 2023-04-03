const addCountry = $("#buttonAddCountry");
const updateCountry = $("#buttonUpdateCountry");
const deleteCountry = $("#buttonDeleteCountry");
const labelCountryName = $("#labelCountryName");
const labelCountryCode = $("#labelCountryCode");
const fieldCountryName = $("#fieldCountryName");
const fieldCountryCode = $("#fieldCountryCode");
const countriesSelect = $("#countriesSelect");
const loadButton = $("#loadButton");
const countryFields = document.getElementById('countryFields');

$(document).ready(function () {
    loadButton.click(() => loadCountries());
    countriesSelect.change(() => changeFormStateToSelectedCountry());
    addCountry.click(() => {
       if (addCountry.val() == 'Add') addNewCountry();
       else changeFormStateToNew();
    });
    updateCountry.click(() => updateSelectedCountry(countriesSelect.val().split("-")[0]));

    deleteCountry.click(() => deleteSelectedCountry(countriesSelect.val().split("-")[0]));
});

function deleteSelectedCountry(countryId) {
  const url = contextPath + "/delete/" + countryId;
  const countryName = fieldCountryName.val();
  const countryCode = fieldCountryCode.val();
  const jsonData = {name: countryName, code: countryCode};

  $.ajax({
      type: 'DELETE',
      url: url,
      beforeSend: (xhr) => xhr.setRequestHeader(csrfHeaderName, csrfValue),
      data: JSON.stringify(jsonData),
      contentType: 'application/json'
  }).done((countryId) => {
      showToastMessage(`The country has been deleted successfully.`);
      $("#countriesSelect option:selected").remove();
      fieldCountryName.val("");
      fieldCountryCode.val("");
  });

}

function updateSelectedCountry(countryId) {
   if (!countryFields.checkValidity()) {
      countryFields.reportValidity();
      return;
   }

  const url = contextPath + "/update/" + countryId;
  const countryName = fieldCountryName.val();
  const countryCode = fieldCountryCode.val();
  const jsonData = {name: countryName, code: countryCode};

  $.ajax({
      type: 'PUT',
      url: url,
      beforeSend: (xhr) => xhr.setRequestHeader(csrfHeaderName, csrfValue),
      data: JSON.stringify(jsonData),
      contentType: 'application/json'
  }).done((countryId) => {
      showToastMessage(`The country has been updated successfully.`);
      $("#countriesSelect option:selected").text(countryName);
  });
}

function addNewCountry() {
   if (!countryFields.checkValidity()) {
      countryFields.reportValidity();
      return;
   }

  const url = contextPath + "/save";
  const countryName = fieldCountryName.val();
  const countryCode = fieldCountryCode.val();
  const jsonData = {name: countryName, code: countryCode};

  $.ajax({
      type: 'POST',
      url: url,
      beforeSend: (xhr) => xhr.setRequestHeader(csrfHeaderName, csrfValue),
      data: JSON.stringify(jsonData),
      contentType: 'application/json'
  }).done((countryId) => {
      selectNewCountry(countryId, countryName, countryCode);
      showToastMessage(`The country has been saved successfully.`)
  });
}

function selectNewCountry(countryId, countryName, countryCode) {
  const value = countryId + "-" + countryCode;
  $("<option>")
    .val(value)
    .text(countryName)
    .appendTo(countriesSelect);

  $("#countriesSelect option[value='" + value + "']").prop("selected", true);
  fieldCountryCode.val("");
  fieldCountryName.val("").focus();
}

function changeFormStateToNew() {
  addCountry.val("Add");
  updateCountry.prop("disabled", true);
  deleteCountry.prop("disabled", true);

  fieldCountryCode.val("");
  fieldCountryName.val("").focus();
}

function changeFormStateToSelectedCountry() {
    addCountry.prop("value", "New");
    updateCountry.prop("disabled", false);
    deleteCountry.prop("disabled", false);

    const selectedCountryName = $("#countriesSelect option:selected").text();
    fieldCountryName.val(selectedCountryName);
    const selectedCountryCode = countriesSelect.val().split("-")[1];
    fieldCountryCode.val(selectedCountryCode);
}

function loadCountries() {
    const url = contextPath + "/list";
    clearTimeout();
    showToastMessage("All countries have been loaded.");

    $.get(url, response => {
        countriesSelect.empty();

        $.each(response, (i, country) => {
          const optionValue = country.id + "-" + country.code;
          $("<option>")
            .val(optionValue)
            .text(country.name)
            .appendTo(countriesSelect);
        });
    }).done(() => {
        loadButton.val("Refresh Country List");
    }).fail(() => {
        showToastMessage("ERROR: Could not connect to server or server encountered an error.");
    });
}

function showToastMessage(message) {
  $("#toastMessage").text(message);
  const toast = $("#toast");
  toast.fadeIn(500);
  setTimeout(() => toast.fadeOut(3000), 6500);
}