$(document).ready(function () {
  $("#LogoutLink").click((e) => {
    e.preventDefault();
    document.LogoutForm.submit();
  });

  // Script for Cancel button
  $("#buttonCancel").click(() => {
    window.location = moduleURL;
  });

  customizeDropDownMenu();

  const dropdownCategories = $("#parentIds");
  const divChosenCategories = $("#chosenCategories");

  dropdownCategories.change(() => {
    divChosenCategories.empty();
    showChosenCategories(dropdownCategories, divChosenCategories);
  });

  $('#shortDescription').richText();
  $('#fullDescription').richText();
});

function showChosenCategories(dropdownCategories, divChosenCategories) {
  dropdownCategories.children("option:selected").each(function () {
    const selectedCategory = $(this);
    const catName = selectedCategory.text().replace(/-/g, "");

    divChosenCategories.append(
      "<span class='badge text-bg-secondary m-1'>" + catName + "</span>"
    );
  });
}

function customizeDropDownMenu() {
  $(".navbar .dropdown").hover(
    function () {
      $(this)
        .find(".dropdown-menu")
        .first()
        .stop(true, true)
        .delay(250)
        .slideDown(200);
    },
    function () {
      $(this)
        .find(".dropdown-menu")
        .first()
        .stop(true, true)
        .delay(100)
        .slideUp(200);
    }
  );

  $(".dropdown > a").click(function () {
    location.href = this.href;
  });
}
