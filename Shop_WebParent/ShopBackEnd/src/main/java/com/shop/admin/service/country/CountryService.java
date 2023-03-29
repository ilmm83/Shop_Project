package com.shop.admin.service.country;

import com.shop.admin.exception.country.CountryNotFoundException;
import com.shop.admin.repository.country.CountryRepository;
import com.shop.model.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CountryService {

    private final CountryRepository repository;

    public List<Country> listAllCountriesOrderByNameAsc() {
        return (List<Country>) repository.findAllByOrderByNameAsc();
    }

    @Transactional
    public Integer save(Country country) {
        return repository.save(country).getId();
    }

    @Transactional
    public Integer delete(Integer id) {
        repository.deleteById(id);
        return id;
    }

    @Transactional
    public Integer update(Integer id, Country country) throws CountryNotFoundException {
        var found = repository.findById(id).orElseThrow(() -> new CountryNotFoundException("Could not found a country with ID: " + id));
        found.setName(country.getName());
        found.setCode(country.getCode());

        return save(found);
    }
}
