$(document).ready(function () {
  $("#dropdownCategory").change(() => $("#searchForm").submit());
});

function clearSearch() {
  window.location = moduleURL;
}
