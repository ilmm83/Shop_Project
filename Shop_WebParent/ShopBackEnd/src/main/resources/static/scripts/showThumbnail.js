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

function removeExtraImage(index) {
  clearExtraImageSection(index);
}

function clearExtraImageSection(index) {
  const html = `
  <div class="p-2 col" id="divExtraImage${index}">
  <div id="extraImageHeader${index}" class="m-2">
    <label>Extra Image #${index}:</label>
    <a
      class="btn fas fa-times-circle fa-2xl text-secondary float-end"
      title="Remove this image"
      href="javascript:removeExtraImage(${index})"
    ></a>
  </div>
  <div class="m-2">
    <img id="extraThumbnail${index}" alt="" class="img-fluid" src="" />
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
