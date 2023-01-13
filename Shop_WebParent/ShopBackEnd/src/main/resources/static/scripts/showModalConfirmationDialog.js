function showModalConfirmationDialog(link) {
  const modal = $("#confirmModal");
  const cancelBtn = $("#ConfirmationBtn");
  const cancelBtnX = $("#modalCancelBtnX");
  const yesBtn = $("#yesBtn");
  const userId = link.attr("userId");
  const deleteUrl = link.attr("href");

  $("#modalBody").text(
    `Are you sure you want delete the user with ID: ${userId}?`
  );
  
  modal.show();
  cancelBtn.click(() => modal.hide());
  cancelBtnX.click(() => modal.hide());
  yesBtn.click(() => yesBtn.attr("href", deleteUrl));

  window.onclick = (event) => {
    if (event.target == modal) modal.hide();
  };
}
