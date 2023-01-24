$(document).ready(() => {
  $("#LogoutLink").click((e) => {
    e.preventDefault();
    document.LogoutForm.submit();
  });
});