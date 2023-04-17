package com.shop.admin.controller.product;

import com.shop.admin.service.product.ProductService;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.model.Product;
import com.shop.model.ProductDetail;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;

public class ProductSaveHelper {
 
  static void setProductDetails(String[] detailNames, String[] detailValues, Product product, ProductService productService) {
    if (detailNames == null)
      return;

    var details = new LinkedList<ProductDetail>();
    for (int i = 0; i < detailNames.length; i++) {
      var name = detailNames[i];
      var value = detailValues[i];

      if (!name.isEmpty() && !value.isEmpty())
        details.add(new ProductDetail(name, value, product));
    }
    if (product.getId() != null)
      productService.clearProductDetails(product.getId());
    product.setDetails(details);
  }

  static void setAndSaveNewExtraImages(MultipartFile[] multipart, Product product) throws IOException {
    if (multipart.length == 0)
      return;

    var uploadDir = "E:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\"
        + product.getId() + "\\extras";
    for (var image : multipart) {
      if (image.isEmpty())
        continue;
      var fileName = StringUtils.cleanPath(image.getOriginalFilename());

      if (product.containsImageName(fileName))
        continue;
      FileUploadUtil.saveFileWithoutClearingForlder(uploadDir, fileName, image);
      product.addExtraImage(fileName);
    }
  }

  static void setAndSaveMainImage(MultipartFile mainImage, Product product) throws IOException {
    if (mainImage.getOriginalFilename() == "")
      return;

    var fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());
    var uploadDir = "E:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\" + product.getId();
    FileUploadUtil.saveFile(uploadDir, fileName, mainImage);
    product.setMainImage(fileName);
  }

  static void isDescriptionsEmpty(Product product, String empty) {
    if (product.getFullDescription().equals(empty))
      product.setFullDescription("No description.");
    if (product.getShortDescription().equals(empty))
      product.setShortDescription("No description.");
  }

}
