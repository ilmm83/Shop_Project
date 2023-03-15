$(document).ready(function() {
  let mainImage = $('#mainImage');
  $('.image-thumbnail').click(function() {
    const currentImageSource = $(this).attr('src');
    const currentImageIndex = $(this).attr('index');

    mainImage.attr('src', currentImageSource);
    mainImage.attr('index', currentImageIndex);
  });

  showSlideshow(mainImage);
});

function showSlideshow(mainImage) {
  const modal = $("#carouselModal");
  const close = $('#modalCancelBtnX');

  mainImage.click(function() {
     const imageIndex = parseInt(mainImage.attr('index'));
     $('#myCarousel').carousel(imageIndex);
     modal.show();
  });
  close.click(function() { modal.hide(); });

  window.onclick = (event) => {
    if (event.target == modal) modal.hide();
  };
}