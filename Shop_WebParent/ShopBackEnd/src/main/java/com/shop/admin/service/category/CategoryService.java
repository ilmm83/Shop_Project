package com.shop.admin.service.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.admin.exception.category.CategoryNotFoundException;
import com.shop.admin.exception.user.UserNotFoundException;
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
        var catFromDB = categoryRepository.findAll();

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

    public List<Category> findAllCategoriesSortedById() {
        return (List<Category>) categoryRepository.findAll(Sort.by("id").ascending());
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

    public Category findById(Long id) throws UserNotFoundException {
        Category found = categoryRepository.findById(id);
        if (found == null)
            throw new UserNotFoundException("Could not find the user with id " + id);
        return found;
    }

    @Transactional
    public Category save(Category formCategory) {
        return categoryRepository.save(formCategory);
    }

    @Transactional
    public void delete(Long id) throws CategoryNotFoundException {
        Long counted = categoryRepository.countById(id);
        if (counted == null || counted == 0)
            throw new CategoryNotFoundException("Could not find category with this ID " + id);
        categoryRepository.deleteById(id);
    }

    @Transactional
    public void changeEnableState(Long id, boolean isEnable) {
        categoryRepository.updateEnabledStatus(id, isEnable);
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    
    private void listChildren(Category parent, List<Category> catToForm) {
        var children = parent.getChildren();
        var dashes = "--";
        for (var sub : children) {
            for (int i = 0; i < children.size(); i++) dashes += "--";
            catToForm.add(new Category(sub.getId(), dashes + sub.getName()));
            listChildren(sub, catToForm);
            children.remove(sub);
        }
    }

}