package com.shop.admin.category;

import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.utils.FileNotSavedException;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;

    public static final int PAGE_SIZE = 4;

    public List<Category> listCategoriesHierarchical() {
        var catToForm = new ArrayList<Category>();
        var catFromDB = repository.findAll(Sort.by("name").ascending());

        for (var category : catFromDB) {
            if (category.getParent() == null) {
                catToForm.add(new Category(category.getId(), category.getName()));
                for (var sub : category.getChildren()) {
                    catToForm.add(new Category(sub.getId(), "--" + sub.getName()));
                    listChildren(sub, catToForm);
                }
            }
        }
        return catToForm;
    }

    public void findAllCategoriesSortedBy(int pageNum, PagingAndSortingHelper helper) {
        helper.searchEntities(pageNum, PAGE_SIZE, repository);
    }

    public List<Category> findAllCategoriesSortedByName() {
        Sort sort = Sort.by("name").ascending();
        return (List<Category>) repository.findAll(sort);
    }

    public Page<Category> listByPage(int pageNum, String sortField, String sortDirection, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

        if (keyword != null)
            return repository.findAll(keyword, pageable);
        else
            return repository.findAll(pageable);
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

    @Transactional
    private Category save(Category catFromForm) {
        var parentProxy = catFromForm.getParent();
        if (parentProxy != null) {
            var parent = repository.findById(parentProxy.getId()).get();
            var allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
            allParentIds += String.valueOf(parent.getId()) + "-";
            catFromForm.setAllParentIDs(allParentIds);
        }

        return repository.save(catFromForm);
    }

    @Transactional
    public void delete(Long id) {
        repository.countById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find category with this ID " + id));
        repository.deleteById(id);
    }

    @Transactional
    public void changeEnableState(Long id, boolean isEnabled) {
        repository.updateEnabledStatus(id, isEnabled);
    }

    public List<Category> findAll() {
        return (List<Category>) repository.findAll();
    }

    public String checkNameAndAliasUnique(Long id, String name, String alias) {
        var categories = repository.findByNameAndAlias(name, alias);
        var response = "OK";

        for (var cat : categories) {
            if (!response.equals("OK"))
                break;
            if (cat == null)
                continue;
            response = isCategoryExistsByNameOrAlias(id, cat, name, alias);
        }

        return response;
    }

    private void listChildren(Category parent, List<Category> catToForm) {
        var children = parent.getChildren();
        var dashes = "--";
        for (var sub : children) {
            for (int i = 0; i < children.size(); i++)
                dashes += "--";
            catToForm.add(new Category(sub.getId(), dashes + sub.getName()));
            listChildren(sub, catToForm);
            children.remove(sub);
        }
    }

    private String isCategoryExistsByNameOrAlias(Long id, Category category, String name, String alias) {
        if (category == null || category.getId() == id)
            return "OK";
        else if (category.getName().equals(name))
            return "Name";
        else if (category.getAlias().equals(alias))
            return "Alias";
        return "OK";
    }
}