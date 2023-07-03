package com.shop.admin.product;

import com.common.model.Product;
import com.shop.admin.brand.BrandNotFoundException;
import com.shop.admin.category.CategoryNotFoundException;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.security.ShopUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static com.shop.admin.product.ProductSaveHelper.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository repository;

    public static final int PAGE_SIZE = 4;


    public void findAllProductsSortedBy(int pageNum, PagingAndSortingHelper helper, Long categoryId) {
        helper.searchEntities(pageNum, PAGE_SIZE, repository, categoryId);
    }

    public String checkNameAndAliasUnique(Long id, String name, String alias) {
        var products = repository.findByNameAndAlias(name, alias);
        var response = "OK";

        for (var product : products) {
            if (!response.equals("OK")) break;
            if (product == null) continue;

            response = isProductExistsByNameOrAlias(id, product, name, alias);
        }

        return response;
    }

    public String createNewProduct(MultipartFile mainImageMultipart, MultipartFile[] extraImagesMultipart,
                                   String[] detailNames, String[] detailValues, ShopUserDetails loggedUser,
                                   RedirectAttributes attributes, Product product) {
        try {
            save(product);

            if (!loggedUser.hasRole("Admin") &&
                !loggedUser.hasRole("Editor") &&
                loggedUser.hasRole("Salesperson")) {

                saveProductPrice(product);
                attributes.addFlashAttribute("message", "The product has been saved successfully.");

                return "redirect:/api/v1/products";
            }

            setAndSaveMainImage(mainImageMultipart, product);
            setAndSaveNewExtraImages(extraImagesMultipart, product);
            setProductDetails(detailNames, detailValues, product, this);

            save(product);

            attributes.addFlashAttribute("message", "The product has been saved successfully.");

        } catch (BrandNotFoundException | CategoryNotFoundException | IOException | ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }

        return "redirect:/api/v1/products";
    }

    public void deleteProduct(Long id, RedirectAttributes attributes) {
        try {
            delete(id);

            var uploadDir = "./Shop_WebParent/product-images/" + id;
            FileUtils.deleteQuietly(new File(uploadDir));

            attributes.addFlashAttribute("message", "The product with ID: " + id + " has been deleted successfully.");

        } catch (ProductNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            e.printStackTrace();
        }
    }

    public Product findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Could not found product with ID: " + id));
    }

    @Transactional
    public void clearProductDetails(Long id) {
        repository.clearProductDetails(id);
    }

    @Transactional
    public void changeProductState(Long id, boolean state) {
        repository.changeProductState(id, state);
    }

    @Transactional
    private void delete(Long id) {
        repository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Could not found product with ID: " + id));

        repository.deleteById(id);
    }

    @Transactional
    public void removeImage(Long productId, String fileName) {
        repository.removeImageByProductId(productId, fileName);

        var uploadDir = "./Shop_WebParent/product-images/" + productId + "/extras/" + fileName;
        FileUtils.deleteQuietly(new File(uploadDir));
    }

    @Transactional
    public Optional<Product> save(Product product) {
        if (product.getId() == null) {
            product.setCreatedAt(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            product.setAlias(product.getName().replaceAll(" ", "-"));
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }

        product.setUpdatedAt(new Date());

        return repository.save(product);
    }

    @Transactional
    public void saveProductPrice(Product productFromForm) {
        var productInDB = repository.findById(productFromForm.getId()).get();
        productInDB.setPrice(productFromForm.getPrice());
        productInDB.setCost(productFromForm.getCost());
        productInDB.setDiscountPercent(productFromForm.getDiscountPercent());

        repository.save(productInDB);
    }


    private String isProductExistsByNameOrAlias(Long id, Product product, String name, String alias) {
        if (product == null || Objects.equals(product.getId(), id)) {
            return "OK";
        } else if (product.getName().equals(name)) {
            return "Name";
        } else if (product.getAlias().equals(alias)) {
            return "Alias";
        }

        return "OK";
    }
}
