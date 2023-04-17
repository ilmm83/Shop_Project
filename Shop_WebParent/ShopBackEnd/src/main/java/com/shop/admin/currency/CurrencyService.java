package com.shop.admin.currency;

import com.shop.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencyService {

    private final CurrencyRepository repository;

    public Currency findById(Integer id) throws CurrencyNotFound {
        return repository.findById(id)
                .orElseThrow(() -> new CurrencyNotFound("Could not find a currency with id: " + id));
    }

    public List<Currency> findAllByOrderByIdAsc() {
        return (List<Currency>) repository.findAllByOrderByIdAsc();
    }
}
