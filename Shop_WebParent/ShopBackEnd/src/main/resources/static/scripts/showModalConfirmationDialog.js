
function showDetailsModal(link) {
  const modal = $("#detailModal");
  const cancelBtn = $("#closeDetailModal");
  const cancelBtnX = $("#detailCancelBtnX");

  modal.show().find(".modal-content").load(link);
  cancelBtn.click(() => modal.hide());
  cancelBtnX.click(() => modal.hide());

  window.onclick = (event) => {
    if (event.target == modal) modal.hide();
  };
}

function showModalConfirmationDialog(link, entityName, entityId) {
  const modal = $("#confirmModal");
  const cancelBtn = $("#ConfirmationBtn");
  const cancelBtnX = $("#modalCancelBtnX");
  const yesBtn = $("#yesBtn");
  const deleteUrl = link.attr("href");

  $("#modalBody").text(
    `Are you sure you want delete the ${entityName} with ID: ${entityId}?`
  );

  modal.show();
  cancelBtn.click(() => modal.hide());
  cancelBtnX.click(() => modal.hide());
  yesBtn.click(() => yesBtn.attr("href", deleteUrl));

  window.onclick = (event) => {
    if (event.target == modal) modal.hide();
  };
}
