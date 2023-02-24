$(document).ready(function () {
  let amount = $("#divExtraImages").attr("imagesAmount");
  if (amount == null) amount = 0;

  for (let i = Number(amount) + 1; i < 8; i++) {
    addExtraImageSection(i);
  }
});

function showThumbnail(id, input, maxSize) {
  if (input.files && input.files[0]) {
    const img = input.files[0];
    if (img.size > maxSize) {
      alert("File size: " + img.size);
      input.setCustomValidity(
        `You must choose an image less than ${maxSize} bytes`
      );
      input.reportValidity();
    } else {
      input.setCustomValidity("");
      const reader = new FileReader();
      reader.onload = (event) => {
        $(id).attr("src", event.target.result).width(150).height(150);
      };
      reader.readAsDataURL(img);
    }
  }
}

function showExtraThumbnail(id, input, maxSize) {
  if (input.files && input.files[0]) {
    const img = input.files[0];
    if (img.size > maxSize) {
      alert("File size: " + img.size);
      input.setCustomValidity(
        `You must choose an image less than ${maxSize} bytes`
      );
      input.reportValidity();
    } else {
      input.setCustomValidity("");
      const reader = new FileReader();
      reader.onload = (event) => {
        $(id).attr("src", event.target.result).width(150).height(150);
      };
      reader.readAsDataURL(img);
    }
  }
}

function addExtraImageSection(i) {
  const html = `
    <div class="p-2 col" id="divExtraImage${i}"
          style="max-width: 320px"
    >
            <div id="extraImageHeader${i}" class="m-2">
              <label
                >Extra Image #${i}:</label
              >
              <a
                class="btn fas fa-times-circle fa-2xl text-secondary float-end"
                title="Remove this image"
                href="javascript:removeExtraImage(${i})"
              ></a>
            </div>
            <div class="m-2">
              <img
                id="extraThumbnail${i}"
                class="img-fluid"
                alt=""
                src=""
              />
            </div>
            <div class="mt-3 ms-2">
              <input
                type="file"
                onchange="showExtraThumbnail('#extraThumbnail${i}', this, 502400)"
                value="Choose File"
                accept="image/png, image/jpeg"
                id="extraImage${i}"
                name="extraImage"
              />
            </div>
      </div>
`;
  $("#divExtraImages").append(html);
}

function clearExtraImageSection(index) {
  const html = `
  <div class="p-2 col" id="divExtraImage${index}"
        style="max-width: 320px"
  >
  <div id="extraImageHeader${index}" class="m-2">
    <label
      >Extra Image #${index}:</label
    >
    <a
      class="btn fas fa-times-circle fa-2xl text-secondary float-end"
      title="Remove this image"
      href="javascript:removeExtraImage(${index})"
    ></a>
  </div>
  <div class="m-2">
    <img
      id="extraThumbnail${index}"
      class="img-fluid"
      alt=""
      src=""
    />
  </div>
  <div class="mt-3 ms-2">
    <input
      type="file"
      onchange="showThumbnail('#extraThumbnail${index}', this, 502400)"
      value="Choose File"
      accept="image/png, image/jpeg"
      id="extraImage${index}"
      name="extraImage"
    />
  </div>
</div>
    `;

  $(`#divExtraImage${index}`).replaceWith(html);
}

function removeExtraImage(index) {
  const url = moduleURL + "/remove_image";
  const fileName = $(`#removeButtonId${index}`).attr("fileName");
  const productId = $(`#removeButtonId${index}`).attr("productId");
  const csrf = $("input[name='_csrf']").val();
  const params = { id: productId, fileName: fileName, _csrf: csrf};

  if (fileName !== null && productId !== null) $.post(url, params);

  clearExtraImageSection(index);
}
