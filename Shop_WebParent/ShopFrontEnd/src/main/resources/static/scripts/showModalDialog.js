function showModalDialog (title, message) {
  const modal = document.getElementById('modalDialog');
  const cancelBtn = document.getElementById('modalCancelBtn');
  const cancelBtnX = document.getElementById('modalCancelBtnX');

  modal.style.display = 'block';
  document.getElementById('modalTitle').innerHTML = title; 
  document.getElementById('modalBody').innerHTML = message;


  cancelBtn.onclick = () => modal.style.display = 'none';
  cancelBtnX.onclick = () => modal.style.display = 'none';

  window.onclick = (event) => {
    if (event.target == modal) modal.style.display = 'none';
  }
}