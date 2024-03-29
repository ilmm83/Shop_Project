package com.shop.admin.product;

import com.common.model.Product;
import com.common.model.ProductDetail;
import com.shop.admin.utils.FileUploadUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class ProductSaveHelper {

    public static void setProductDetails(String[] detailNames, String[] detailValues, Product product, ProductService productService) {
        if (detailNames == null) {
            return;
        }

        var details = new LinkedList<ProductDetail>();

        for (int i = 0; i < detailNames.length; i++) {
            var name = detailNames[i];
            var value = detailValues[i];

            if (!name.isEmpty() && !value.isEmpty()) {
                details.add(new ProductDetail(name, value, product));
            }
        }
        if (product.getId() != null) {
            productService.clearProductDetails(product.getId());
        }

        product.setDetails(details);
    }

    public static void setAndSaveNewExtraImages(MultipartFile[] multipart, Product product) throws IOException {
        if (multipart.length == 0) {
            return;
        }

        var uploadDir = "./Shop_WebParent/product-images/" + product.getId() + "/extras";

        for (var image : multipart) {
            if (image.isEmpty()) continue;

            var fileName = StringUtils.cleanPath(image.getOriginalFilename());

            if (product.containsImageName(fileName)) continue;

            FileUploadUtil.saveFileWithoutClearingFolder(uploadDir, fileName, image);

            product.addExtraImage(fileName);
        }
    }

    public static void setAndSaveMainImage(MultipartFile mainImage, Product product) throws IOException {
        if (Objects.equals(mainImage.getOriginalFilename(), "")) {
            return;
        }

        var fileName = StringUtils.cleanPath(mainImage.getOriginalFilename());
        var uploadDir = "./Shop_WebParent/product-images/" + product.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, mainImage);

        product.setMainImage(fileName);
    }

    public static void isDescriptionsEmpty(Product product, String empty) {
        if (product.getFullDescription().equals(empty)) {
            product.setFullDescription("No description.");
        }
        if (product.getShortDescription().equals(empty)) {
            product.setShortDescription("No description.");
        }
    }
}
