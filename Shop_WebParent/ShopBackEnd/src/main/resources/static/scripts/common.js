$(document).ready(function() {
  $("#LogoutLink").click((e) => {
    e.preventDefault();
    document.LogoutForm.submit();
  });

  // Script for Cancel button
  $("#buttonCancel").click(() => {
    window.location = moduleURL;
  });

  customizeDropDownMenu();
});

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

  $(".dropdown > a").click(function() {
    location.href = this.href;
  });
}
