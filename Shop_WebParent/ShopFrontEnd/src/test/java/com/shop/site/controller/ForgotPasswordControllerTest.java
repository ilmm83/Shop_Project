package com.shop.site.controller;

import com.shop.site.customer.CustomerService;
import com.shop.site.customer.EmailSettingBag;
import com.shop.site.customer.ForgotPasswordController;
import com.shop.site.setting.SettingService;
import com.shop.site.util.Utility;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

public class ForgotPasswordControllerTest {

    @Mock
    CustomerService customerService;

    @Mock
    SettingService settingService;

    @Mock
    Model model;

    ForgotPasswordController controller;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new ForgotPasswordController(customerService, settingService);
    }

    @Test
    void canViewPasswordRecoveryPage() {
        // given
        var expectedViewName = "customers/password/forgot_password_form";

        // when
        var viewName = controller.viewPasswordRecoveryPage();

        // then
        assertEquals(expectedViewName, viewName);
    }

    @Test
    void canViewNewPasswordPage_Success() {
        // given
        var expectedViewName = "customers/password/new_password_form";
        var expectedToken = "token";

        given(customerService.checkResetPasswordToken(expectedToken)).willReturn(true);
        given(model.getAttribute("token")).willReturn(expectedToken);

        // when
        var viewName = controller.viewNewPasswordPage(expectedToken, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedToken, model.getAttribute("token"));
    }

    @Test
    void canViewNewPasswordPage_Fail() {
        // given
        var expectedViewName = "customers/password/invalid_token";
        var expectedToken = "token";

        given(customerService.checkResetPasswordToken(expectedToken)).willReturn(false);
        given(model.getAttribute("token")).willReturn(expectedToken);

        // when
        var viewName = controller.viewNewPasswordPage(expectedToken, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedToken, model.getAttribute("token"));
    }

    @Test
    void canSaveNewPassword() {
        // given
        var password = "pass";
        var expectedViewName = "redirect:/login";
        var expectedToken = "token";

        willDoNothing().given(customerService).updateCustomerPassword(expectedToken, password);
        given(model.getAttribute("token")).willReturn(expectedToken);

        // when
        var viewName = controller.saveNewPassword(password, expectedToken);

        // then
        verify(customerService, times(1)).updateCustomerPassword(expectedToken, password);
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedToken, model.getAttribute("token"));
    }

    @Test
    void canProcessRequestForm_Success() {
        // given
        var expectedEmail = "test-email";
        var expectedViewName = "customers/password/forgot_password_form";
        var expectedSettings = mock(EmailSettingBag.class);
        var expectedMessage = "We have sent you a reset password link to your email.";
        var expectedUrl = "url";
        var request = mock(HttpServletRequest.class);
        var utility = mockStatic(Utility.class);
        var message = mock(MimeMessage.class);
        var sender = mock(JavaMailSenderImpl.class);

        given(request.getParameter("email")).willReturn(expectedEmail);
        given(customerService.updateResetPasswordToken(expectedEmail)).willReturn(expectedMessage);
        given(settingService.getEmailSettings()).willReturn(expectedSettings);
        given(expectedSettings.getFromAddress()).willReturn("from address");
        given(expectedSettings.getSenderName()).willReturn("Sender name");
        given(expectedSettings.getCustomerVerifySubject()).willReturn("verify subject");
        given(sender.createMimeMessage()).willReturn(message);
        given(model.getAttribute("message")).willReturn(expectedMessage);

        utility.when(() -> Utility.getSiteURL(request)).thenReturn(expectedUrl);
        utility.when(() -> Utility.prepareMailSender(expectedSettings)).thenReturn(sender);

        willDoNothing().given(sender).send(message);

        // when
        var viewName = controller.processRequestForm(request, model);

        // then
        verify(customerService, times(1)).updateResetPasswordToken(expectedEmail);
        verify(settingService, times(1)).getEmailSettings();
        verify(sender, times(1)).send(message);
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedMessage, model.getAttribute("message"));

        utility.close();
    }

}
