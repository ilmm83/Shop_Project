package com.shop.admin.repository.state;

import com.shop.model.Country;
import com.shop.model.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {

    Iterable<State> findByCountryOrderByNameAsc(Country country);
}
