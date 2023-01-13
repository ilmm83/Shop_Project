function showThumbnail(input) {
  if (input.files && input.files[0]) {
    const img = input.files[0];
    if (img.size > 1048576) {
      alert('File size: ' + img.size);
      input.setCustomValidity('You must choose an image less than 1MB');
      input.reportValidity();
    } 
    else {
      input.setCustomValidity('');
      const reader = new FileReader();
      reader.onload = (event) => {
        $('#thumbnail')
          .attr('src', event.target.result)
          .width(150)
          .height(200);
      };
      reader.readAsDataURL(img);
    }
  }
}