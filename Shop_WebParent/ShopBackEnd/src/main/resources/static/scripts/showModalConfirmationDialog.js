function showModalConfirmationDialog() {
  const modal = document.getElementById('confirmModal');
  const cancelBtn = document.getElementById('ConfirmationBtn');
  const cancelBtnX = document.getElementById('modalCancelBtnX');
  const yesBtn = document.getElementById('yesBtn');
  const DeleteUrl = document.getElementsByClassName('link-delete').item(0).getAttribute('href');


  $("#modalBody").text("Are you sure you want to delete this user?");
  modal.style.display = 'block';

  cancelBtn.onclick = () => modal.style.display = 'none';
  cancelBtnX.onclick = () => modal.style.display = 'none';
  yesBtn.onclick = () => yesBtn.setAttribute('href', DeleteUrl);

  window.onclick = (event) => {
    if (event.target == modal)
      modal.style.display = 'none';
  }
}