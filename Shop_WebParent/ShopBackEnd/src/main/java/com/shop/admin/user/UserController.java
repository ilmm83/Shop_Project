package com.shop.admin.user;

import com.common.model.User;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.paging.PagingAndSortingParam;
import com.shop.admin.utils.exporter.UserCsvExporter;
import com.shop.admin.utils.exporter.UserExcelExporter;
import com.shop.admin.utils.exporter.UserPDFExporter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    private static final String REDIRECT_API_V1_USERS = "redirect:/api/v1/users";


    @GetMapping
    public String listFirstPage() {
        return "redirect:/api/v1/users/1?sortField=id&sortDir=asc";
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        var users = service.findAllUsersSortedByFirstName();
        new UserCsvExporter().export(users, response);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        var users = service.findAllUsersSortedByFirstName();
        new UserExcelExporter().export(users, response);
    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        var users = service.findAllUsersSortedById();
        new UserPDFExporter().export(users, response);
    }

    @GetMapping("/{pageNum}")
    public String listByPage(@PagingAndSortingParam(listName = "users", moduleURL = "/api/v1/users") PagingAndSortingHelper helper, @PathVariable int pageNum) {
        service.listByPage(pageNum, helper);

        return "users/users";
    }

    @GetMapping("/new")
    public String viewUserFormPage(Model model) {
        var user = new User();
        user.setEnabled(true);

        model.addAttribute("roles", service.findAllRoles());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create New User");
        model.addAttribute("moduleURL", "/api/v1/users");

        return "users/user_form";
    }

    @PostMapping("/save")
    public String createNewUser(@RequestParam("image") MultipartFile multipart, User user, RedirectAttributes redirect) {
        service.createNewUser(multipart, user);

        redirect.addFlashAttribute("message", "The user has been saved successfully.");

        var firstPartOfEmail = user.getEmail().split("@")[0];
        return REDIRECT_API_V1_USERS + "/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("user", service.findById(id));
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            model.addAttribute("roles", service.findAllRoles());
            model.addAttribute("moduleURL", "/api/v1/users");

            return "users/user_form";

        } catch (UserNotFoundException e) {
            e.printStackTrace();
            redirect.addFlashAttribute("message", e.getMessage());
        }

        return REDIRECT_API_V1_USERS;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            service.delete(id);

            redirect.addFlashAttribute("message", "The user with ID " + id + " has been deleted successfuly");

        } catch (UserNotFoundException e) {
            redirect.addFlashAttribute("message", e.getMessage());
        }

        return REDIRECT_API_V1_USERS;
    }

    @GetMapping("/enabled/true/{id}")
    public String changeEnableStateToEnabled(@PathVariable("id") Long id, RedirectAttributes redirect) {
        redirect.addFlashAttribute("message", "User with ID: " + id + " is now Enabled.");

        service.changeEnableState(id, true);

        return REDIRECT_API_V1_USERS;
    }

    @GetMapping("/enabled/false/{id}")
    public String changeEnableStateToDisabled(@PathVariable("id") Long id, RedirectAttributes redirect) {
        redirect.addFlashAttribute("message", "User with ID: " + id + " is now Disabled.");

        service.changeEnableState(id, false);

        return REDIRECT_API_V1_USERS;
    }
}
