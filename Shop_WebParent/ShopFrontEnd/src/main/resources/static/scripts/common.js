$(document).ready(function() {
  const dropdownCountry = $('#country');
  const dataListState = $('#listStates');
  const fieldState = $('#state');

  dropdownCountry.change(() => {
    loadStatesForCountry(dataListState, dropdownCountry);
    fieldState.val('').focus();
  });
})

function loadStatesForCountry(dataListState, dropdownCountry) {
  const countryId = $('#country option:selected').val();
  const url = contextPath + "states/list/" + countryId;

  if (countryId == '0') {
      dataListState.empty();
      return;
  }

  $.get(url, (responseJSON) => {
      dataListState.empty();

      $.each(responseJSON, (i, state) => {
          $('<option>')
            .val(state.name)
            .text(state.name)
            .appendTo(dataListState);
      });
  });
}