package com.shop.admin.controller.customer;

import com.shop.admin.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerRestController {

    private final CustomerService service;


    @PostMapping("/check_email")
    public String isDuplicatedEmail(@Param("id") Long id, @Param("email") String email) {
        return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
