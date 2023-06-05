package com.shop.site.state;

import com.common.model.Country;
import com.common.model.State;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends CrudRepository<State, Integer> {

    List<State> findByCountryOrderByNameAsc(Country country);
}
