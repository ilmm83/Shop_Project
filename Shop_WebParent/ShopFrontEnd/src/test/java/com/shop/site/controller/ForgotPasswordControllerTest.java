package com.shop.site.controller;

import com.shop.site.customer.CustomerService;
import com.shop.site.customer.ForgotPasswordController;
import com.shop.site.setting.SettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

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

}
