package com.shop.admin.brand;

import com.common.model.Brand;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.utils.FileNotSavedException;
import com.shop.admin.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository repository;

    public static final int PAGE_SIZE = 4;


    @Transactional
    private Optional<Brand> save(Brand brand) {
        return repository.save(brand);
    }

    @Transactional
    public Brand deleteById(Long id) {
        repository.countById(id).orElseThrow(() -> new BrandNotFoundException("Could not find brand with this ID " + id));

        return repository.deleteById(id);
    }

    public Brand findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new BrandNotFoundException("Could not find brand with this ID " + id));
    }

    public List<Brand> findAllByNameAsc() {
        return (List<Brand>) repository.findAllByNameAsc();
    }

    public void findAllBrandsSortedBy(int pageNum, PagingAndSortingHelper helper) {
        helper.searchEntities(pageNum, PAGE_SIZE, repository);
    }

    public String checkNameUnique(Long id, String name) {
        var brands = repository.findByName(name);
        var response = "OK";

        for (var brand : brands) {
            if (!response.equals("OK")) break;
            if (brand == null) continue;
            response = isBrandExistsByName(id, brand, name);
        }

        return response;
    }

    private String isBrandExistsByName(Long id, Brand brand, String name) {
        if (brand == null || Objects.equals(brand.getId(), id)) return "OK";
        else if (brand.getName().equals(name)) return "brand";

        return "OK";
    }

    public void createNewBrand(Brand brand, MultipartFile multipart) {
        try {
            if (!multipart.isEmpty()) {
                var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
                brand.setLogo(fileName);

                var saved = save(brand).orElseThrow(() -> new BrandNotFoundException("Brand was not saved."));

                var uploadDir = "./Shop_WebParent/brands-images/" + saved.getId();
                FileUploadUtil.saveFile(uploadDir, fileName, multipart);

            } else {
                save(brand);
            }
        } catch (IOException e) {
            throw new FileNotSavedException(e.getMessage(), e);
        }
    }
}
