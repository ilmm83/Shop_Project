const addState = $("#buttonAddState");
const updateState = $("#buttonUpdateState");
const deleteState = $("#buttonDeleteState");
const labelStateName = $("#labelStateName");
const fieldStateName = $("#fieldStateName");
const countriesSelectForStates = $("#countriesSelectForStates");
const loadButtonForStates = $("#loadButtonForStates");
const statesSelect = $("#statesSelect");


$(document).ready(function () {
    loadButtonForStates.click(() => loadCountriesForStates());
    countriesSelectForStates.change(() => loadStates());
    statesSelect.change(() => changeFormStateToSelectedState());
    addState.click(() => {
       if (addState.val() == 'Add')
          addNewState();
       else
          changeFormStateToNew();
    });
    updateState.click(() => updateSelectedState());

    deleteState.click(() => deleteSelectedState());
});

function deleteSelectedState() {
  const selectedState = $("#statesSelect option:selected");
  const url = statesPath + "/delete/" + selectedState.val().split("-")[0];

  $.ajax({
      type: 'DELETE',
      url: url,
      beforeSend: (xhr) => xhr.setRequestHeader(csrfHeaderName, csrfValue),
  }).done((stateID) => {
      showToastMessage(`The country has been deleted successfully.`);
      selectedState.remove();
      fieldStateName.val("");
  });

}

function updateSelectedState() {
  const stateName = fieldStateName.val();
  const selectedCountryId = $("#countriesSelectForStates option:selected").val();
  const url = statesPath + "/update/" + $("#statesSelect option:selected").val().split("-")[0];
  const jsonData = {name: stateName, country: selectedCountryId};

  $.ajax({
      type: 'PUT',
      url: url,
      beforeSend: (xhr) => xhr.setRequestHeader(csrfHeaderName, csrfValue),
      data: JSON.stringify(jsonData),
      contentType: 'application/json'
  }).done((stateId) => {
      showToastMessage(`The state has been updated successfully.`);
      $("#statesSelect option:selected").text(stateName);
  });
}

function addNewState() {
  const url = statesPath + "/save";
  const stateName = fieldStateName.val();
  const selectedCountryId = $("#countriesSelectForStates option:selected").val();
  const jsonData = {name: stateName, country: selectedCountryId};

  $.ajax({
      type: 'POST',
      url: url,
      beforeSend: (xhr) => xhr.setRequestHeader(csrfHeaderName, csrfValue),
      data: JSON.stringify(jsonData),
      contentType: 'application/json'
  }).done((stateId) => {
      selectNewState(stateId, stateName);
      showToastMessage(`The state has been saved successfully.`)
  });
}

function selectNewState(stateId, stateName) {
  $("<option>")
    .val(stateId)
    .text(stateName)
    .appendTo(statesSelect);

  $("#statesSelect option[value='" + stateId + "']").prop("selected", true);
  fieldStateName.val("").focus();
}

function changeFormStateToNew() {
  addState.val("Add");
  updateState.prop("disabled", true);
  deleteState.prop("disabled", true);

  fieldStateName.val("").focus();
}

function changeFormStateToSelectedState() {
    addState.prop("value", "New");
    updateState.prop("disabled", false);
    deleteState.prop("disabled", false);

    const selectedStateName = $("#statesSelect option:selected").text();
    fieldStateName.val(selectedStateName);
}

function loadStates() {
    const selectedCountry = $("#countriesSelectForStates option:selected");
    const countryId = selectedCountry.val().split("-")[0];
    const url = statesPath + "/list/" + countryId;

    if (countryId == '0') {
        statesSelect.empty();
        return;
    }

    clearTimeout();
    showToastMessage("All countries have been loaded.");

    $.get(url, response => {
        statesSelect.empty();

        $.each(response, (i, country) => {
          const optionValue = country.id + "-" + country.code;
          $("<option>")
            .val(optionValue)
            .text(country.name)
            .appendTo(statesSelect);
        });
    }).done(() => {
        loadButtonForStates.val("Refresh Country List");
    }).fail(() => {
        showToastMessage("ERROR: Could not connect to server or server encountered an error.");
    });
}

function loadCountriesForStates() {
    const url = contextPath + "/list";
    clearTimeout();
    showToastMessage("All countries have been loaded.");

    $.get(url, response => {
        countriesSelectForStates.empty();

        $.each(response, (i, country) => {
          const optionValue = country.id + "-" + country.code;
          $("<option>")
            .val(optionValue)
            .text(country.name)
            .appendTo(countriesSelectForStates);
        });
    }).done(() => {
        loadButtonForStates.val("Refresh Country List");
        loadStates();
    }).fail(() => {
        showToastMessage("ERROR: Could not connect to server or server encountered an error.");
    });

}