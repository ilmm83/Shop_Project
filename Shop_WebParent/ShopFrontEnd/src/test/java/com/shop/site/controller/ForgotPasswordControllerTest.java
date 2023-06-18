package com.shop.site.controller;

import com.shop.site.customer.CustomerService;
import com.shop.site.customer.ForgotPasswordController;
import com.shop.site.setting.SettingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForgotPasswordControllerTest {

    @Mock
    CustomerService customerService;

    @Mock
    SettingService settingService;

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
}
