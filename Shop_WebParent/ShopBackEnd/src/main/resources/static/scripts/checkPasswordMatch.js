function checkPasswordMatch(confirm_password) {
  if (confirm_password.value != $('#password').val()) 
    confirm_password.setCustomValidity("Password do not match!");
  else 
    confirm_password.setCustomValidity("");
}