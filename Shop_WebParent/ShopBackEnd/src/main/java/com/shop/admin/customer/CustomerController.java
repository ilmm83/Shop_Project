package com.shop.admin.customer;

import com.common.model.Customer;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.paging.PagingAndSortingParam;
import com.shop.admin.utils.exporter.CustomerCSVExporter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;


    @GetMapping
    public String viewFirstPage() {
        return "redirect:/api/v1/customers/1?sortField=id&sortDir=asc";
    }

    @GetMapping("/{pageNum}")
    public String viewByPage(@PagingAndSortingParam(listName = "customers", moduleURL = "/api/v1/customers") PagingAndSortingHelper helper,
                             @PathVariable int pageNum) {

        customerService.listByPage(pageNum, helper);

        return "customers/customers";
    }

    @GetMapping("/enabled/true/{id}")
    public String enableCustomer(@PathVariable long id, Model model) {
        customerService.enable(id, true);

        model.addAttribute("message", "Customer with ID: " + id + " is enabled.");

        return "redirect:/api/v1/customers";
    }

    @GetMapping("/enabled/false/{id}")
    public String disableCustomer(@PathVariable long id, Model model) {
        customerService.enable(id, false);

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
        model.addAttribute("moduleURL", "/api/v1/customers");

        return "customers/customer_edit_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomerById(@PathVariable long id, Model model) {
        try {
            customerService.deleteById(id);

            model.addAttribute("message", "The customer with ID: " + id + " has deleted successfully.");

        } catch (CustomerNotFoundException e) {
            model.addAttribute("message", "The customer with ID: " + id + " does not exist.");
        }

        return "redirect:/api/v1/customers";
    }

    @PostMapping("/update")
    public String updateCustomer(Customer customer) {
        customerService.update(customer);

        return "redirect:/api/v1/customers";
    }
}
