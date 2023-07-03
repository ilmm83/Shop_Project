package com.shop.admin.controller;

import com.common.model.User;
import com.shop.admin.security.ShopUserDetails;
import com.shop.admin.user.AccountController;
import com.shop.admin.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    @Mock
    UserService service;

    @Mock
    ShopUserDetails loggedUser;

    AccountController controller;


    @BeforeEach
    void setup() {
        controller = new AccountController(service);
    }

    @Test
    void canViewAccountDetails() {
        // given
        var email = "email";
        var model = mock(Model.class);
        var expectedUser = new User();
        var expectedViewName = "users/account_form";

        given(loggedUser.getUsername()).willReturn(email);
        given(service.getByEmail(anyString())).willReturn(expectedUser);
        given(model.getAttribute("user")).willReturn(expectedUser);

        // when
        var viewName = controller.viewAccountDetails(loggedUser, model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedUser, model.getAttribute("user"));
    }

    @Test
    void canUpdateUserAccount() {
        // given
        var multipart = mock(MultipartFile.class);
        var attributes = mock(RedirectAttributes.class);
        var updatedUser = new User();
        var expectedViewName = "redirect:/api/v1/account";
        var expectedMessage = "The user has been updated successfully.";

        willDoNothing().given(service).updateUserAccount(multipart, updatedUser, loggedUser);
        given(attributes.getAttribute("message")).willReturn(expectedMessage);

        // when
        var viewName = controller.updateUserAccount(multipart, loggedUser, updatedUser, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(service).updateUserAccount(multipart, updatedUser, loggedUser);
    }
}
