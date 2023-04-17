package com.shop.admin.brand;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shop.admin.brand.BrandNotFoundException;
import com.shop.admin.brand.BrandRepository;
import com.shop.model.Brand;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BrandService {

  private final BrandRepository repository;

  public static final int PAGE_SIZE = 4;

  @Transactional
  public Optional<Brand> save(Brand brand) {
    return repository.save(brand);
  }

  @Transactional
  public Brand deleteById(Long id) throws BrandNotFoundException {
    repository.countById(id)
        .orElseThrow(() -> new BrandNotFoundException("Could not find brand with this ID " + id));

    return repository.deleteById(id);
  }

  public Brand findById(Long id) throws BrandNotFoundException {
    return repository.findById(id)
        .orElseThrow(() -> new BrandNotFoundException("Could not find brand with this ID " + id));
  }

  public List<Brand> findAllByNameAsc() {
    return (List<Brand>) repository.findAllByNameAsc();
  }

  public Page<Brand> findAllBrandsSortedBy(String keyword, int pageNum, String field, String direction) {
    Sort sort = Sort.by(field);
    sort = direction.equals("asc") ? sort.ascending() : sort.descending();

    PageRequest pageable = PageRequest.of(pageNum - 1, PAGE_SIZE, sort);

    if (keyword != null)
      return repository.findAll(keyword, pageable);
    else
      return repository.findAll(pageable);
  }

  public String checkNameUnique(Long id, String name) {
    var brands = repository.findByName(name);
    var response = "OK";

    for (var brand : brands) {
      if (!response.equals("OK"))
        break;
      if (brand == null)
        continue;
      response = isBrandExistsByName(id, brand, name);
    }
    return response;
  }

  private String isBrandExistsByName(Long id, Brand brand, String name) {
    if (brand == null || brand.getId() == id)
      return "OK";
    else if (brand.getName().equals(name))
      return "brand";
    return "OK";
  }
}
