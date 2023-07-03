package com.shop.admin.user;

import com.common.model.User;
import com.shop.admin.security.ShopUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final UserService service;


    @GetMapping
    public String viewAccountDetails(@AuthenticationPrincipal ShopUserDetails loggedUser, Model model) {
        var email = loggedUser.getUsername();
        var user = service.getByEmail(email);

        model.addAttribute("user", user);

        return "users/account_form";
    }

    @PostMapping("/update")
    public String updateUserAccount(@RequestParam("image") MultipartFile multipart,
                                    @AuthenticationPrincipal ShopUserDetails loggedUser,
                                    User user, RedirectAttributes attributes) {

        service.updateUserAccount(multipart, user, loggedUser);

        attributes.addFlashAttribute("message", "The user has been updated successfully.");

        return "redirect:/api/v1/account";
    }
}
