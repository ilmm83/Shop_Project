$(document).ready(function () {
  const dropdownBrandsPF = $("#brandId");
  const dropdownCategoriesPF = $("#categoryId");

  dropdownBrandsPF.change(() => {
    dropdownCategoriesPF.empty();
    getCategories(dropdownBrandsPF, dropdownCategoriesPF, brandModuleURL);
  });
  getCategories(dropdownBrandsPF, dropdownCategoriesPF, brandModuleURL);
});

function getCategories(dropdownBrands, dropdownCategories, brandModuleURL) {
  const brandId = dropdownBrands.val();
  if (brandId === undefined) return;

  const url = brandModuleURL + "/categories/" + brandId ;

  $.get(url, (response) => {
    $.each(response, (index, category) => {
      $("<option>")
        .val(category.id)
        .text(category.name)
        .appendTo(dropdownCategories);
    });
  });
}
