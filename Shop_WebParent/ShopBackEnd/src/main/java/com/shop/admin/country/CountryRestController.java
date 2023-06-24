package com.shop.admin.country;

import com.common.model.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/countries")
public class CountryRestController {

    private final CountryService service;


    @GetMapping("/list")
    public ResponseEntity<List<Country>> listAllCountries() {
        return ResponseEntity.ok(service.listAllCountriesOrderByNameAsc());
    }

    @PostMapping("/save")
    public ResponseEntity<Integer> save(@RequestBody Country country) {
        return new ResponseEntity<>(service.save(country), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Integer> update(@PathVariable Integer id, @RequestBody Country country) {
        return new ResponseEntity<>(service.update(id, country), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Integer id) {
        return new ResponseEntity<>(service.delete(id), HttpStatus.OK);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    private ResponseEntity<CountryExceptionResponse> authRegisterExceptionHandler(CountryNotFoundException e) {
        var response = new CountryExceptionResponse(e.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
