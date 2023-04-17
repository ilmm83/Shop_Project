package com.shop.site.state;

import com.shop.dto.StateDTO;
import com.shop.model.Country;
import com.shop.model.State;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/states")
public class StateRestController {

    private final StateRepository repository;

    @GetMapping("/list/{country_id}")
    public ResponseEntity<List<StateDTO>> listAllByCountry(@PathVariable Integer country_id) {
        return ResponseEntity.ok((repository.findByCountryOrderByNameAsc(new Country(country_id)).stream()
                .map(this::convertToStateDTO)
                .collect(Collectors.toList())));
    }

    private StateDTO convertToStateDTO(State state) {
        var dto = new StateDTO();
        dto.setId(state.getId());
        dto.setName(state.getName());

        return dto;
    }
}
