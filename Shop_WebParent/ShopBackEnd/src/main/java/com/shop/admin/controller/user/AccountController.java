package com.shop.admin.controller.user;

import java.io.IOException;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.exception.user.UserNotFoundException;
import com.shop.admin.security.user.ShopUserDetails;
import com.shop.admin.service.user.UserService;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.model.User;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

  private final UserService service;

  @GetMapping
  public String viewDetails(@AuthenticationPrincipal ShopUserDetails loggedUser, Model model)
      throws UserNotFoundException {
        
    String email = loggedUser.getUsername();
    User user = service.getByEmail(email);

    model.addAttribute("user", user);

    return "users/account_form";
  }

  @PostMapping("/update")
  public String createNewUser(User user, RedirectAttributes redirect,
      @RequestParam("image") MultipartFile file, @AuthenticationPrincipal ShopUserDetails userDetails)
      throws IOException, UserNotFoundException {

    if (!file.isEmpty()) {
      String fileName = StringUtils.cleanPath(file.getOriginalFilename());
      user.setPhotos(fileName);
      User saved = service.updateUserAccount(user);
      String uploadDir = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\ShopBackEnd\\src\\main\\resources\\static\\images\\user-images\\"
          + saved.getId();
      FileUploadUtil.saveFile(uploadDir, fileName, file);
    } else {
      if (user.getPhotos().isEmpty())
        user.setPhotos(null);
      service.updateUserAccount(user);
    }

    userDetails.setFirstName(user.getFirstName());
    userDetails.setLastName(user.getLastName());

    redirect.addFlashAttribute("message", "The user has been updated successfuly.");

    return "redirect:/api/v1/account";
  }

}
