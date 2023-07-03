package com.shop.admin.category;

import com.common.model.Category;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.utils.FileNotSavedException;
import com.shop.admin.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;

    public static final int PAGE_SIZE = 4;


    public List<Category> listCategoriesHierarchical() {
        var formCategory = new ArrayList<Category>();
        var DBCategory = repository.findAll(Sort.by("name").ascending());

        for (var category : DBCategory) {
            if (category.getParent() == null) {
                formCategory.add(new Category(category.getId(), category.getName()));

                for (var sub : category.getChildren()) {
                    formCategory.add(new Category(sub.getId(), "--" + sub.getName()));

                    listChildren(sub, formCategory);
                }
            }
        }

        return formCategory;
    }

    public void findAllCategoriesSortedBy(int pageNum, PagingAndSortingHelper helper) {
        helper.searchEntities(pageNum, PAGE_SIZE, repository);
    }

    public List<Category> findAllCategoriesSortedByName() {
        return (List<Category>) repository.findAll(Sort.by("name").ascending());
    }

    public Category findById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new CategoryNotFoundException("Could not find category with this ID " + id));
    }

    public void createNewCategory(MultipartFile multipart, Category category) {
        if (!multipart.isEmpty()) {
            var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
            category.setImage(fileName);

            var saved = save(category);
            var uploadDir = "./Shop_WebParent/categories-images/" + saved.getId();

            try {
                FileUploadUtil.saveFile(uploadDir, fileName, multipart);

            } catch (IOException e) {
                throw new FileNotSavedException(e.getMessage(), e);
            }
        } else {
            save(category);
        }
    }

    public String checkNameAndAliasUnique(Long id, String name, String alias) {
        var categories = repository.findByNameAndAlias(name, alias);
        var response = "OK";

        for (var cat : categories) {
            if (!response.equals("OK")) break;
            if (cat == null) continue;

            response = isCategoryExistsByNameOrAlias(id, cat, name, alias);
        }

        return response;
    }

    @Transactional
    private Category save(Category formCategory) {
        var parentProxy = formCategory.getParent();

        if (parentProxy != null) {
            var parent = repository.findById(parentProxy.getId()).get();
            var allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += parent.getId() + "-";

            formCategory.setAllParentIDs(allParentIds);
        }

        return repository.save(formCategory);
    }

    @Transactional
    public void delete(Long id) {
        repository.countById(id).orElseThrow(() -> new CategoryNotFoundException("Could not find category with this ID " + id));
        repository.deleteById(id);
    }

    @Transactional
    public void changeEnableState(Long id, boolean isEnabled) {
        repository.updateEnabledStatus(id, isEnabled);
    }


    private void listChildren(Category parent, List<Category> categories) {
        var children = parent.getChildren();
        var dashes = new StringBuilder("--");

        for (var sub : children) {
            dashes.append("--".repeat(children.size()));

            categories.add(new Category(sub.getId(), dashes + sub.getName()));
            listChildren(sub, categories);

            children.remove(sub);
        }
    }

    private String isCategoryExistsByNameOrAlias(Long id, Category category, String name, String alias) {
        if (category == null || Objects.equals(category.getId(), id)) return "OK";
        else if (category.getName().equals(name)) return "Name";
        else if (category.getAlias().equals(alias)) return "Alias";

        return "OK";
    }
}