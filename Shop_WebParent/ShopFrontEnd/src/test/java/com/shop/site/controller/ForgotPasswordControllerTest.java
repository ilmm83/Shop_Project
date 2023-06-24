package com.shop.site.controller;

import com.shop.site.customer.CustomerService;
import com.shop.site.customer.EmailSettingBag;
import com.shop.site.customer.ForgotPasswordController;
import com.shop.site.setting.SettingService;
import com.shop.site.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

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

        willDoNothing().given(customerService).updatePassword(expectedToken, password);
        given(model.getAttribute("token")).willReturn(expectedToken);

        // when
        var viewName = controller.saveNewPassword(password, expectedToken);

        // then
        verify(customerService, times(1)).updatePassword(expectedToken, password);
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

    @Test
    void canProcessRequestForm_Fail() throws MessagingException, UnsupportedEncodingException {
        // given
        var expectedEmail = "test-email";
        var expectedSettings = mock(EmailSettingBag.class);
        var expectedMessage = "We have sent you a reset password link to your email.";
        var expectedError = "Could not send an email.";
        var expectedViewName = "customers/password/forgot_password_form";
        var expectedUrl = "url";
        var request = mock(HttpServletRequest.class);
        var utility = mockStatic(Utility.class);
        var message = mock(MimeMessage.class);
        var sender = mock(JavaMailSenderImpl.class);
        var helper = mock(MimeMessageHelper.class);

        given(request.getParameter("email")).willReturn(expectedEmail);
        given(customerService.updateResetPasswordToken(expectedEmail)).willReturn(expectedMessage);
        given(settingService.getEmailSettings()).willReturn(expectedSettings);
        given(expectedSettings.getSenderName()).willReturn("Sender name");
        given(expectedSettings.getCustomerVerifySubject()).willReturn("verify subject");
        given(expectedSettings.getFromAddress()).willReturn("From address");
        given(sender.createMimeMessage()).willReturn(message);
        given(model.getAttribute("message")).willReturn(expectedMessage);
        given(model.getAttribute("error")).willReturn(expectedError);

        utility.when(() -> Utility.getSiteURL(request)).thenReturn(expectedUrl);
        utility.when(() -> Utility.prepareMailSender(expectedSettings)).thenReturn(sender);

        willThrow(new MessagingException()).given(helper).setFrom(anyString(), anyString());
        willDoNothing().given(sender).send(message);

        // when
        var viewName = controller.processRequestForm(request, model);

        // then
        verify(customerService, times(1)).updateResetPasswordToken(expectedEmail);
        verify(settingService, times(1)).getEmailSettings();
        verify(sender, times(1)).send(message);
        assertEquals(expectedMessage, model.getAttribute("message"));
        assertEquals(expectedError, model.getAttribute("error"));
        assertEquals(expectedViewName, viewName);
        assertThrows(MessagingException.class, () -> helper.setFrom(expectedSettings.getFromAddress(), expectedSettings.getSenderName()));

        utility.close();
    }
}
