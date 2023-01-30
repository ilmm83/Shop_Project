function showModalConfirmationDialog(link, entity, entityId) {
  const modal = $("#confirmModal");
  const cancelBtn = $("#ConfirmationBtn");
  const cancelBtnX = $("#modalCancelBtnX");
  const yesBtn = $("#yesBtn");
  const deleteUrl = link.attr("href");

  $("#modalBody").text(
    `Are you sure you want delete the ${entity} with ID: ${entityId}?`
  );
  
  modal.show();
  cancelBtn.click(() => modal.hide());
  cancelBtnX.click(() => modal.hide());
  yesBtn.click(() => yesBtn.attr("href", deleteUrl));

  window.onclick = (event) => {
    if (event.target == modal) modal.hide();
  };
}
