package com.shop.site.customer;

import com.shop.site.setting.SettingService;
import com.shop.site.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final CustomerService customerService;
    private final SettingService settingService;


    @GetMapping("/forgot")
    public String viewPasswordRecoveryPage() {
        return "customers/password/forgot_password_form";
    }

    @GetMapping("/reset")
    public String viewNewPasswordPage(@Param("token") String token, Model model) {
        model.addAttribute("token", token);
        return customerService.checkResetPasswordToken(token) ? "customers/password/new_password_form" : "customers/password/invalid_token";
    }

    @PostMapping("/new")
    public String saveNewPassword(@Param("password") String password, @Param("token") String token) {
        customerService.updatePassword(token, password);
        return "redirect:/login";
    }

    @PostMapping("/forgot")
    public String processRequestForm(HttpServletRequest request, Model model) {
        try {
            var email = request.getParameter("email");
            var token = customerService.updateResetPasswordToken(email);

            sendResetPasswordVerificationEmail(request, token, email);
            model.addAttribute("message", "We have sent you a reset password link to your email.");
        } catch (MessagingException | UnsupportedEncodingException e) {
            model.addAttribute("error", "Could not send an email.");
        }

        return "customers/password/forgot_password_form";
    }


    private void sendResetPasswordVerificationEmail(HttpServletRequest request, String token, String email) throws MessagingException, UnsupportedEncodingException {
        var settings = settingService.getEmailSettings();
        var url = Utility.getSiteURL(request) + "/password/reset?token=" + token;
        var sender = Utility.prepareMailSender(settings);
        var message = sender.createMimeMessage();
        var helper = new MimeMessageHelper(message);
        var content = "<p>Hello,</p>"
            + "You have requested to reset your password."
            + "Click the link below to change your password:</p>"
            + "<p><a href=\"" + url + "\">Change password.</a>"
            + "<p>Ignore this message if you do remember your password, "
            + "or you don't made the request.</p>";

        helper.setTo(email);
        helper.setFrom(settings.getFromAddress(), settings.getSenderName());
        helper.setSubject(settings.getCustomerVerifySubject());
        helper.setText(content, true);

        sender.send(message);
    }
}
