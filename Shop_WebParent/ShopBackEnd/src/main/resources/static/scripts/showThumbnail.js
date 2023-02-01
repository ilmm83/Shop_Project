function showThumbnail(input) {
  if (input.files && input.files[0]) {
    const img = input.files[0];
    if (img.size > 102400) {
      alert('File size: ' + img.size);
      input.setCustomValidity('You must choose an image less than 100KB');
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