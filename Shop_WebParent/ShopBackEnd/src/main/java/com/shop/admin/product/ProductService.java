
package com.shop.admin.product;

import java.io.File;
import java.util.Date;
import java.util.Optional;

import com.shop.admin.paging.PagingAndSortingHelper;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.admin.product.ProductNotFoundException;
import com.shop.admin.product.ProductRepository;
import com.shop.model.Product;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public static final int PAGE_SIZE = 4;

    public void findAllProductsSortedBy(int pageNum, PagingAndSortingHelper helper, Long categoryId) {
        helper.searchEntities(pageNum, PAGE_SIZE, repository, categoryId);
    }

    @Transactional
    public Optional<Product> save(Product product) {
        if (product.getId() == null)
            product.setCreatedAt(new Date());

        if (product.getAlias() == null || product.getAlias().isEmpty())
            product.setAlias(product.getName().replaceAll(" ", "-"));
        else
            product.setAlias(product.getAlias().replaceAll(" ", "-"));

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

    @Transactional
    public void clearProductDetails(Long id) {
        repository.clearProductDetails(id);
    }

    @Transactional
    public void changeProductState(Long id, boolean state) {
        repository.changeProductState(id, state);
    }

    @Transactional
    public void deleteProduct(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Could not found product with ID: " + id));
        repository.deleteById(id);
    }

    @Transactional
    public void removeImage(Long productId, String fileName) {
        repository.removeImageByProductId(productId, fileName);
        var uploadDir = "E:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\product-images\\" + productId
                + "\\extras\\" + fileName;
        FileUtils.deleteQuietly(new File(uploadDir));
    }

    public String checkNameAndAliasUnique(Long id, String name, String alias) {
        var products = repository.findByNameAndAlias(name, alias);
        var response = "OK";

        for (var cat : products) {
            if (!response.equals("OK"))
                break;
            if (cat == null)
                continue;
            response = isProductExistsByNameOrAlias(id, cat, name, alias);
        }

        return response;
    }

    private String isProductExistsByNameOrAlias(Long id, Product product, String name, String alias) {
        if (product == null || product.getId() == id)
            return "OK";
        else if (product.getName().equals(name))
            return "Name";
        else if (product.getAlias().equals(alias))
            return "Alias";
        return "OK";
    }

    public Product findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Could not found product with ID: " + id));
    }

}
