package com.shop.site.category;

import com.common.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository repository;


    public List<Category> listNoChildrenCategories() {
        var noChildren = new ArrayList<Category>();

        repository.findAllEnabledCategories().stream()
                .filter(category -> category != null || category.getChildren().size() == 0)
                .forEach(noChildren::add);

        return noChildren;
    }

    public Category getCategoryByAlias(String alias)  {
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
