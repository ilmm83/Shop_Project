package com.shop.admin.controller.user;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shop.admin.exception.user.UserNotFoundException;
import com.shop.admin.service.user.UserService;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.admin.utils.exporter.user.UserCsvExporter;
import com.shop.admin.utils.exporter.user.UserExcelExporter;
import com.shop.admin.utils.exporter.user.UserPDFExporter;
import com.shop.model.User;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    private static final String REDIRECT_API_V1_USERS = "redirect:/api/v1/users";

    @GetMapping
    public String listFirstPage(Model model) {
        Page<User> page = service.listByPage(1, "id", "asc", null);
        changingDisplayUsersPage(1, model, page, "id", "asc", null);
        return "users/users";
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        var users = service.findAllUsersSortedByFirstName();
        UserCsvExporter exporter = new UserCsvExporter();
        exporter.export(users, response);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        var users = service.findAllUsersSortedByFirstName();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(users, response);
    }

    @GetMapping("/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        var users = service.findAllUsersSortedById();
        UserPDFExporter exporter = new UserPDFExporter();
        exporter.export(users, response);
    }

    @GetMapping("/{pageNum}")
    public String listByPage(@PathVariable("pageNum") int pageNum, @Param("sortField") String sortField,
            @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {

        Page<User> page = service.listByPage(pageNum, sortField, sortDir, keyword);
        changingDisplayUsersPage(pageNum, model, page, sortField, sortDir, keyword);
        return "users/users";
    }

    @GetMapping("/user_form")
    public String signIn(Model model) {
        User user = new User();
        user.setEnabled(true);

        model.addAttribute("roles", service.findAllRoles());
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Create New User");
        return "users/user_form";
    }

    @PostMapping("/create")
    public String createNewUser(User user, RedirectAttributes redirect, @RequestParam("image") MultipartFile file)
            throws IOException {

        if (!file.isEmpty()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            user.setPhotos(fileName);
            User saved = service.save(user);
            String uploadDir = "F:\\Projects\\JavaProjects\\Shop_Project\\Shop_WebParent\\ShopBackEnd\\src\\main\\resources\\static\\images\\user-images\\"
                    + saved.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, file);
        } else {
            if (user.getPhotos().isEmpty())
                user.setPhotos(null);
            service.save(user);
        }

        redirect.addFlashAttribute("message", "The user has been saved successfuly.");

        String firstPartOfEmail = user.getEmail().split("@")[0];
        return REDIRECT_API_V1_USERS + "/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("user", service.findById(id));
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");
            model.addAttribute("roles", service.findAllRoles());
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

    @GetMapping("/{id}/enabled/true")
    public String changeEnableStateToEnabled(@PathVariable("id") Long id, RedirectAttributes redirect) {
        redirect.addFlashAttribute("message", "User with ID: " + id + " is now Enabled.");
        service.changeEnableState(id, true);
        return REDIRECT_API_V1_USERS;
    }

    @GetMapping("/{id}/enabled/false")
    public String changeEnableStateToDisabled(@PathVariable("id") Long id, RedirectAttributes redirect) {
        redirect.addFlashAttribute("message", "User with ID: " + id + " is now Disabled.");
        service.changeEnableState(id, false);
        return REDIRECT_API_V1_USERS;
    }


    private void changingDisplayUsersPage(int pageNum, Model model, Page<User> page, String sortField, String sortDir,
            String keyword) {

        String reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("keyword", keyword);
        model.addAttribute("users", page.getContent());
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reverseSortOrder", reverseSortOrder);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("lastPage", (page.getTotalElements() / UserService.PAGE_SIZE) + 1);
        model.addAttribute("totalUsers", page.getTotalElements());
    }
}
