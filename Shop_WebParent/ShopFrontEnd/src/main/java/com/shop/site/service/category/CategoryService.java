package com.shop.site.service.category;

import com.shop.model.Category;
import com.shop.site.exception.category.CategoryNotFoundException;
import com.shop.site.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;

    public List<Category> listNoChildrenCategories() {
        var noChildren = new ArrayList<Category>();

        repository.findAllEnabledCategories().stream()
                .filter(category -> category != null || category.getChildren().size() == 0)
                .forEach(category -> noChildren.add(category));

        return noChildren;
    }

    public Category getCategoryByAlias(String alias) throws CategoryNotFoundException {
        return repository.findByAliasEnabled(alias)
                .orElseThrow(() -> new CategoryNotFoundException("Could not find a category with alias: " + alias));
    }

    public List<Category> getCategoryParents(Category child) {
        var parents = new ArrayList<Category>();

        var parent = child == null ? null : child.getParent();
        while (parent != null) {
            parents.add(0, parent);
            parent = parent.getParent();
        }
        parents.add(child);

        return parents;
    }
}
