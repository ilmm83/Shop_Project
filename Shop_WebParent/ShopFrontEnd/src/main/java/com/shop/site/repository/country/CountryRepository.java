package com.shop.site.repository.country;

import com.shop.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

    Iterable<Country> findAllByOrderByNameAsc();
}
