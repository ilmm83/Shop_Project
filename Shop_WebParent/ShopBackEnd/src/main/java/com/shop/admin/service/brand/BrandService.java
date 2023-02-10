package com.shop.admin.service.brand;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.admin.repository.brand.BrandRepository;
import com.shop.model.Brand;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {

  private final BrandRepository repository;

  public static final int PAGE_SIZE = 4;

  public Page<Brand> findAllCategoriesSortedBy(String keyword, int pageNum, String field, String direction) {
    Sort sort = Sort.by(field);
    sort = direction.equals("asc") ? sort.ascending() : sort.descending();

    PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

    if (keyword != null)
      return repository.findAll(keyword, pageable);
    else
      return repository.findAll(pageable);
  }

}
