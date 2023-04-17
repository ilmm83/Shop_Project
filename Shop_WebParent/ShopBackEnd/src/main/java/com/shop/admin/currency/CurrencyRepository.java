package com.shop.admin.currency;

import com.shop.model.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Integer> {

    Iterable<Currency> findAllByOrderByIdAsc();
}
