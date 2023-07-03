package com.shop.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserRestController {

    private final UserService service;


    @PostMapping("/api/v1/users/check_email")
    public String isDuplicatedEmail(@Param("id") Long id, @Param("email") String email) {
        return service.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
