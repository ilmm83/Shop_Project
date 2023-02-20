function addMoreDetail() {
  let index = 1;

  const html = `
  <div class="row ps-3 pt-3 border border-bottom-0 border-top-0" id="extraDetail${index}">
  <div class="col col-sm-1">
    <label>Name:</label>
  </div>
  <div class="col col-sm-4">
    <input class="form-control" type="text" name="detailName" maxlength="255"/>
  </div>
  <div class="col col-sm-1">
    <label>Value:</label>
  </div>
  <div class="col col-sm-4">
    <input class="form-control" type="text" name="detailValue" maxlength="255"/>
  </div>
  <div class="col col-auto">
    <button class="btn fas fa-times-circle fa-2xl text-secondary float-end m-2 pt-2" onclick="removeExtraDetail(${index})">
    </button>
  </div>
</div>
  `;

  $("#divDetailsContainer").append(html);
}

function removeExtraDetail(index) {
  $(`#extraDetail${index}`).remove();
}
