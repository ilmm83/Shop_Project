package com.shop.admin.country;

import com.common.model.Country;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends CrudRepository<Country, Integer> {

    Iterable<Country> findAllByOrderByNameAsc();
}
