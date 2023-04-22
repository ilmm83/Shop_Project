package com.shop.site.customer;

import com.shop.model.Customer;
import com.shop.site.setting.SettingService;
import com.shop.site.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final SettingService settingService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("countries", customerService.listAllCountries());
        model.addAttribute("customer", new Customer());
        model.addAttribute("pageTitle", "Customer Registration Page");

        return "customers/customer_form";
    }

    @PostMapping("/save")
    public String registerCustomer(Customer customer, HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        customerService.save(customer);
        sendVerificationEmail(request, customer);

        model.addAttribute("pageTitle", "Registration Succeeded!");
        return "customers/registration_success";
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        var settings = settingService.getEmailSettings();
        var url = Utility.getSiteURL(request) + "/verify?code=" + customer.getVerificationCode();
        var sender = Utility.prepareMailSender(settings);
        var message = sender.createMimeMessage();
        var helper = new MimeMessageHelper(message);
        var content = settings.getCustomerVerifyContent()
                .replace("[[name]]", customer.getFullName())
                .replace("[[URL]]", url);

        helper.setFrom(settings.getFromAddress(), settings.getSenderName());
        helper.setTo(customer.getEmail());
        helper.setSubject(settings.getCustomerVerifySubject());
        helper.setText(content, true);

        sender.send(message);
    }

}
