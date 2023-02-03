package com.shop.admin.service.category;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.admin.exception.category.CategoryNotFoundException;
import com.shop.admin.repository.category.CategoryRepository;
import com.shop.model.Category;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public static final int PAGE_SIZE = 4;

    public List<Category> listCategoriesHierarchal() {
        var catToForm = new ArrayList<Category>();
        var catFromDB = categoryRepository.findAll(Sort.by("name").ascending());

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

    public Page<Category> findAllCategoriesSortedBy(String keyword, int pageNum, String field, String direction) {
        Sort sort = Sort.by(field);
        sort = direction.equals("asc") ? sort.ascending() : sort.descending();

        PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

        if (keyword != null)
            return categoryRepository.findAll(keyword, pageable);
        else
            return categoryRepository.findAll(pageable);
    }

    public Page<Category> listByPage(int pageNum, String sortField, String sortDirection, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();

        PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

        if (keyword != null)
            return categoryRepository.findAll(keyword, pageable);
        else
            return categoryRepository.findAll(pageable);
    }

    public Category findById(Long id) throws CategoryNotFoundException {
        Category found = categoryRepository.findById(id);
        if (found == null)
            throw new CategoryNotFoundException("Could not find the user with id " + id);
        return found;
    }

    @Transactional
    public Category save(Category catFromForm) {
        return categoryRepository.save(catFromForm);
    }

    @Transactional
    public void delete(Long id) throws CategoryNotFoundException {
        Long counted = categoryRepository.countById(id);
        if (counted == null || counted == 0)
            throw new CategoryNotFoundException("Could not find category with this ID " + id);
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void changeEnableState(Long id, boolean isEnabled) {
        categoryRepository.updateEnabledStatus(id, isEnabled);
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public String checkNameAndAliasUnique(Long id, String name, String alias) {
        var categories = categoryRepository.findByNameAndAlias(name, alias);
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
        if (id != null) {
            if (category == null || category.getId() == id)
                return "OK";
            else if (category.getName().equals(name))
                return "Name";
            else if (category.getAlias().equals(alias))
                return "Alias";
        }
        return "OK";
    }
}