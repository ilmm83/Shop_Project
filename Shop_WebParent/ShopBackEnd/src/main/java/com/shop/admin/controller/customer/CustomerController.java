package com.shop.admin.controller.customer;

import com.shop.admin.service.customer.CustomerService;
import com.shop.admin.utils.exporter.customer.CustomerCSVExporter;
import com.shop.model.Customer;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    public String viewFirstPage(Model model) {
        var page = customerService.listByPage(1, "id", "asc", null);
        changingDisplayUsersPage(1, model, page, "id", "asc", null);
        return "customers/customers";
    }

    @GetMapping("/{page_num}")
    public String viewByPage(@PathVariable int page_num, @Param("sortField") String sortField,
                             @Param("sortDir") String sortDir, @Param("keyword") String keyword, Model model) {

        var page = customerService.listByPage(page_num, sortField, sortDir, keyword);
        changingDisplayUsersPage(page_num, model, page, sortField, sortDir, keyword);
        return "customers/customers";
    }

    @GetMapping("/enabled/true/{id}")
    public String enableCustomer(@PathVariable long id, Model model) {
        customerService.enableCustomer(id, true);

        model.addAttribute("message", "Customer with ID: " + id + " is enabled.");
        return "redirect:/api/v1/customers";
    }

    @GetMapping("/enabled/false/{id}")
    public String disableCustomer(@PathVariable long id, Model model) {
        customerService.enableCustomer(id, false);

        model.addAttribute("message", "Customer with ID: " + id + " is disabled.");
        return "redirect:/api/v1/customers";
    }

    @GetMapping("/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        var customers = customerService.findAllUsersSortedByFirstName();
        new CustomerCSVExporter().export(customers, response);
    }

    @GetMapping("/detail/{id}")
    public String viewCustomerDetails(@PathVariable long id, Model model) {
        var customer = customerService.findById(id);

        model.addAttribute("customer", customer);
        return "customers/customer_detail_modal";
    }

    @GetMapping("/edit/{id}")
    public String viewEditCustomerPage(@PathVariable long id, Model model) {
        var found = customerService.findById(id);

        model.addAttribute("customer", found);
        model.addAttribute("pageTitle", "Edit Customer");
        return "customers/customer_edit_form";
    }

    @PostMapping("/update")
    public String updateCustomer(Customer customer) {
        customerService.updateCustomer(customer);

        return "redirect:/api/v1/customers";
    }


    private void changingDisplayUsersPage(int pageNum, Model model, Page<Customer> page,
                                          String sortField, String sortDir, String keyword) {

        String reverseSortOrder = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("keyword", keyword);
        model.addAttribute("customers", page.getContent());
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortField", sortField);
        model.addAttribute("reverseSortOrder", reverseSortOrder);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("lastPage", (page.getTotalElements() / CustomerService.PAGE_SIZE) + 1);
        model.addAttribute("totalPages", page.getTotalElements());
    }
}
