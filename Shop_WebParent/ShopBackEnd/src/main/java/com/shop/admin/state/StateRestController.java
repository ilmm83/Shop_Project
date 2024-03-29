package com.shop.admin.state;

import com.common.dto.StateDTO;
import com.common.model.Country;
import com.common.model.State;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/states")
public class StateRestController {

    private final StateService service;


    @GetMapping("/list/{country_id}")
    public ResponseEntity<List<StateDTO>> findAllStatesByCountry(@PathVariable Integer country_id) {
        return ResponseEntity.ok(service.listByCountry(new Country(country_id)).stream()
            .map(this::convertToStateDTO)
            .collect(Collectors.toList()));
    }

    @PostMapping("/save")
    public ResponseEntity<Integer> save(@RequestBody State state) {
        return new ResponseEntity<>(service.save(state), HttpStatus.CREATED);
    }

    @PutMapping("/update/{state_id}")
    public ResponseEntity<Integer> update(@PathVariable Integer state_id, @RequestBody State state) {
        return new ResponseEntity<>(service.update(state_id, state), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{state_id}")
    public ResponseEntity<Integer> delete(@PathVariable Integer state_id) {
        return new ResponseEntity<>(service.delete(state_id), HttpStatus.OK);
    }


    private StateDTO convertToStateDTO(State state) {
        var dto = new StateDTO();
        dto.setId(state.getId());
        dto.setName(state.getName());

        return dto;
    }
}
