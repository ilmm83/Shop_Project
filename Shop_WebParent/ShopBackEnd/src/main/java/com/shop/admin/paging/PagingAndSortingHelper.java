package com.shop.admin.paging;

import com.shop.admin.product.ProductRepository;
import com.shop.model.Customer;
import com.shop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.method.support.ModelAndViewContainer;

@Getter
@AllArgsConstructor
public class PagingAndSortingHelper {

    private ModelAndViewContainer model;
    private String listName;
    private String sortField;
    private String sortDir;
    private String keyword;

    public void updateModelAttributes(Page<?> page, int pageNum) {
        model.addAttribute(listName, page.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("lastPage", (page.getTotalElements() / page.getSize()) + 1);
        model.addAttribute("totalPages", page.getTotalElements());
    }

    public void searchEntities(int pageNum, int PAGE_SIZE, SearchRepository<?, Long> repository) {
        var sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        var pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);
        Page<?> page;

        if (keyword != null) page = repository.findAll(keyword, pageable);
        else page = repository.findAll(pageable);
        updateModelAttributes(page, pageNum);
    }

    public void searchEntities(int pageNum, int PAGE_SIZE, ProductRepository repository, Long categoryId) {
        var sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        var pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);
        Page<Product> page;

        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                var categoryIdMatch = "-" + categoryId + "-";
                page = repository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable);
            } else
                page = repository.findAll(keyword, pageable);
        } else if (categoryId != null && categoryId > 0) {
            var categoryIdMatch = "-" + categoryId + "-";
            page = repository.findAllInCategory(categoryId, categoryIdMatch, pageable);
        } else
            page = repository.findAll(pageable);
        updateModelAttributes(page, pageNum);
    }
}
