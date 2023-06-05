package com.shop.admin.state;

import com.common.model.Country;
import com.common.model.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {

    Iterable<State> findByCountryOrderByNameAsc(Country country);
}
