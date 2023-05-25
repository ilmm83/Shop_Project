package com.shop.admin.user;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService service;

    @PostMapping("/api/v1/users/check_email")
    public String isDuplicatedEmail(@Param("id") Long id, @Param("email") String email) {
        return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }

}