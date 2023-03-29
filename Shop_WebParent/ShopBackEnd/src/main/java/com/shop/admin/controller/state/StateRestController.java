package com.shop.admin.controller.state;

import com.shop.admin.service.state.StateService;
import com.shop.dto.StateDTO;
import com.shop.model.Country;
import com.shop.model.State;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/states")
@RequiredArgsConstructor
public class StateRestController {

    private final StateService service;


    @GetMapping("/list/{country_id}")
    public ResponseEntity<List<StateDTO>> listAllByCountry(@PathVariable Integer country_id) {
        return ResponseEntity.ok(service.listByCountry(new Country(country_id)).stream()
                .map(this::convertToStateDTO)
                .collect(Collectors.toList()));
    }

    @PostMapping("/save")
    public ResponseEntity<Integer> save(@RequestBody State state) {
        return new ResponseEntity<>(service.save(state), HttpStatus.CREATED);
    }

    @SneakyThrows
    @PutMapping("/update/{state_id}")
    public ResponseEntity<Integer> update(@PathVariable Integer state_id, @RequestBody State state) {
        return new ResponseEntity<>(service.update(state_id, state), HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Integer id) {
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }

    private StateDTO convertToStateDTO(State state) {
        var dto = new StateDTO();
        dto.setId(state.getId());
        dto.setName(state.getName());

        return dto;
    }
}
