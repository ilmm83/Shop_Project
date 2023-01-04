package com.shop.admin.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.admin.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService service;

    @PostMapping("/users/check_email")
    public String isDuplicatedEmail(@Param("email") String email) {
        return service.isEmailExist(email) ? "Duplicated" : "OK";
    }

}
