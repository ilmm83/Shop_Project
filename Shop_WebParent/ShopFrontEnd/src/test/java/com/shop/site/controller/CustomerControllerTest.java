package com.shop.site.controller;

import com.common.model.Country;
import com.common.model.Customer;
import com.shop.site.customer.CustomerController;
import com.shop.site.customer.CustomerService;
import com.shop.site.setting.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    CustomerService customerService;

    @Mock
    SettingService settingService ;

    @Mock
    HttpServletRequest request;

    @Mock
    Model model;

    @Mock
    Principal principal;

    @InjectMocks
    CustomerController controller;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showRegisterForm() {
        // given
        var expectedCountries = List.of(new Country("Country 1", "CC"));
        var expectedPageTitle = "Customer Registration Page";
        var expectedCustomer = new Customer();

        when(customerService.listAllCountries()).thenReturn(expectedCountries);
        when(model.getAttribute("expectedCountries")).thenReturn(expectedCountries);
        when(model.getAttribute("expectedCustomer")).thenReturn(expectedCustomer);
        when(model.getAttribute("pageTitle")).thenReturn(expectedPageTitle);

        // when
        var viewName = controller.showRegisterForm(model);

        // then
        assertEquals(viewName , "customers/customer_form");
        assertEquals(customerService.listAllCountries(), expectedCountries);
        assertEquals(model.getAttribute("expectedCountries"), expectedCountries);
        assertEquals(model.getAttribute("pageTitle"), expectedPageTitle);
        assertNotNull(model.getAttribute("expectedCustomer"));
    }

    @Test
    void viewCustomer_AndShowModelAttributes() {
        // given
        var email = "test@mail.com";
        var expectedCountries = List.of(new Country("Country", "CC"));
        var expectedPageTitle = "Customer Registration Page";
        var expectedCustomer = new Customer();
        expectedCustomer.setEmail(email);

        when(request.getUserPrincipal()).thenReturn(principal);
        when(request.getUserPrincipal().getName()).thenReturn(email);
        when(customerService.listAllCountries()).thenReturn(expectedCountries);
        when(customerService.findByEmail(eq(email))).thenReturn(expectedCustomer);
        when(model.getAttribute(eq("countries"))).thenReturn(expectedCountries);
        when(model.getAttribute(eq("customer"))).thenReturn(expectedCustomer);
        when(model.getAttribute(eq("pageTitle"))).thenReturn(expectedPageTitle);

        // when
        var viewName = controller.viewCustomer(request, model);

        // then
        assertEquals(request.getUserPrincipal().getName(), email);
        assertEquals(expectedCountries, customerService.listAllCountries());
        assertEquals(expectedCustomer, customerService.findByEmail(email));
        assertEquals(expectedCountries, model.getAttribute("countries"));
        assertEquals(expectedCustomer, model.getAttribute("customer"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
        assertEquals("customers/customer_update_form", viewName);
    }

    @Test
    void verifyAccount_VerifyFail() {
        // given
        var code = "code";
        var expectedResult = "customers/verify_fail";

        when(customerService.checkVerificationCode(eq(code))).thenReturn(false);

        // when
        var result = controller.verifyAccount(code);

        // then
        assertFalse(customerService.checkVerificationCode(code));
        assertEquals(expectedResult, result);
    }

    @Test
    void verifyAccount_VerifySuccess() {
        // given
        var code = "code";
        var expectedResult = "customers/verify_success";

        when(customerService.checkVerificationCode(eq(code))).thenReturn(true);

        // when
        var result = controller.verifyAccount(code);

        // then
        assertTrue(customerService.checkVerificationCode(code));
        assertEquals(expectedResult, result);
    }

    @Test
    void updateCustomer() {
        // given
        var updatedCustomer = new Customer();


    }

    @Test
    void registerCustomer() {
    }
}