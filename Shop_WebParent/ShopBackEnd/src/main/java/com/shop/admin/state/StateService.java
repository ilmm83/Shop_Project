package com.shop.admin.state;

import com.shop.model.Country;
import com.shop.model.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StateService {

    private final StateRepository repository;

    public List<State> listByCountry(Country country) {
        return (List<State>) repository.findByCountryOrderByNameAsc(country);
    }

    @Transactional
    public Integer save(State state) {
        return repository.save(state).getId();
    }

    @Transactional
    public Integer update(Integer id, State state)  {
        var found = repository.findById(id)
                .orElseThrow(() -> new StateNotFoundException("Could not find state by ID: " + id));

        found.setName(state.getName());
        found.setCountry(state.getCountry());

        return repository.save(found).getId();
    }

    @Transactional
    public Integer delete(Integer id) {
        repository.deleteById(id);
        return id;
    }

}
