package com.shop.site.customer;

import com.common.model.Customer;
import com.shop.site.security.oauth2.CustomerOAuth2User;
import com.shop.site.setting.SettingService;
import com.shop.site.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @GetMapping("/account")
    public String viewCustomer(HttpServletRequest request, Model model) {
        var email = getCustomerEmailByAuthenticationToken(request);

        model.addAttribute("countries", customerService.listAllCountries());
        model.addAttribute("customer", customerService.findByEmail(email));
        model.addAttribute("pageTitle", "Customer Registration Page");

        return "customers/customer_update_form";
    }

    @GetMapping("/registration_verify")
    public String verifyAccount(@Param("code") String code) {
        return customerService.checkVerificationCode(code) ? "customers/verify_success" : "customers/verify_fail";
    }

    @PostMapping("/update")
    public String updateCustomer(Customer customer) {
        customerService.update(customer);
        return "redirect:/";
    }

    @PostMapping("/save")
    public String registerCustomer(Customer customer, HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {
        customerService.save(customer);
        sendRegistrationVerificationEmail(request, customer);

        model.addAttribute("pageTitle", "Registration Succeeded!");
        return "customers/registration_success";
    }


    private void sendRegistrationVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        var settings = settingService.getEmailSettings();
        var url = Utility.getSiteURL(request) + "/customers/registration_verify?code=" + customer.getVerificationCode();
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

    private String getCustomerEmailByAuthenticationToken(HttpServletRequest request) {
        var token = request.getUserPrincipal();
        String email = null;

        if (token instanceof UsernamePasswordAuthenticationToken || token instanceof RememberMeAuthenticationToken) {
            email = token.getName();
        } else if (token instanceof OAuth2AuthenticationToken) {
            email = ((CustomerOAuth2User) ((OAuth2AuthenticationToken) token).getPrincipal()).getEmail();
        }

        return email;
    }
}
