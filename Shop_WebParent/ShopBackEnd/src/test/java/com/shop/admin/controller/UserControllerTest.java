package com.shop.admin.controller;

import com.common.model.Role;
import com.common.model.User;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.user.UserController;
import com.shop.admin.user.UserNotFoundException;
import com.shop.admin.user.UserService;
import com.shop.admin.utils.exporter.UserCsvExporter;
import com.shop.admin.utils.exporter.UserPDFExporter;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    UserService service;

    @Mock
    HttpServletResponse response;

    @Mock
    RedirectAttributes attributes;

    @Mock
    Model model;

    UserController controller;

    final String REDIRECT_API_V1_USERS = "redirect:/api/v1/users";


    @BeforeEach
    void setup() {
        controller = new UserController(service);
    }

    @Test
    void canViewFirstPage() {
        // given
        var expectedURL = "redirect:/api/v1/users/1?sortField=id&sortDir=asc";

        // when
        var url = controller.viewFirstPage();

        // then
        assertEquals(expectedURL, url);
    }

    @Test
    void canExportToCSV() throws IOException {
        // given
        var user = new User();
        var expectedUsers = List.of(user);
        var exporter = mock(UserCsvExporter.class);
        var writer = mock(PrintWriter.class);

        willReturn(expectedUsers).given(service).findAllUsersSortedByFirstName();
        willReturn(writer).given(response).getWriter();

        // when
        controller.exportToCSV(response);
        exporter.export(expectedUsers, response);

        // then
        verify(exporter, times(1)).export(expectedUsers, response);
        assertEquals(expectedUsers, service.findAllUsersSortedByFirstName());
    }

    @Test
    void canExportToExcel() throws IOException {
        // given
        var user = new User();
        var expectedUsers = List.of(user);
        var exporter = mock(UserCsvExporter.class);
        var os = mock(ServletOutputStream.class);

        given(service.findAllUsersSortedByFirstName()).willReturn(expectedUsers);
        given(response.getOutputStream()).willReturn(os);

        // when
        controller.exportToExcel(response);
        exporter.export(expectedUsers, response);

        // then
        verify(exporter, times(1)).export(expectedUsers, response);
        assertEquals(expectedUsers, service.findAllUsersSortedByFirstName());
    }

    @Test
    void canExportToPDF() throws IOException {
        // given
        var user = new User();
        var expectedUsers = List.of(user);
        var exporter = mock(UserPDFExporter.class);
        var os = mock(ServletOutputStream.class);

        given(service.findAllUsersSortedByFirstName()).willReturn(expectedUsers);
        given(response.getOutputStream()).willReturn(os);

        // when
        controller.exportToPDF(response);
        exporter.export(expectedUsers, response);

        // then
        verify(exporter, times(1)).export(expectedUsers, response);
        assertEquals(expectedUsers, service.findAllUsersSortedByFirstName());
    }

    @Test
    void canViewUsersByP() {
        // given
        var pageNum = 1;
        var helper = mock(PagingAndSortingHelper.class);
        var expectedViewName = "users/users";

        willDoNothing().given(service).findUsersByPageNumber(pageNum, helper);

        // when
        var viewName = controller.viewUsersByPageNumber(helper, pageNum);

        // then
        assertEquals(expectedViewName, viewName);
        verify(service).findUsersByPageNumber(pageNum, helper);
    }

    @Test
    void canViewCreateUserPage() {
        // given
        var expectedRoles = List.of(new Role());
        var expectedPageTitle = "Create New User";
        var expectedModuleURL = "/api/v1/users";
        var expectedUser = new User();
        expectedUser.setEnabled(true);

        given(model.getAttribute("user")).willReturn(expectedUser);
        given(model.getAttribute("roles")).willReturn(expectedRoles);
        given(model.getAttribute("pageTitle")).willReturn(expectedPageTitle);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var viewName = controller.viewCreateUserPage(model);

        // then
        assertEquals("users/user_form", viewName);
        assertEquals(expectedUser, model.getAttribute("user"));
        assertEquals(expectedRoles, model.getAttribute("roles"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
    }

    @Test
    void canCreateNewUser() {
        // given
        var user = mock(User.class);
        var multipart = mock(MultipartFile.class);
        var firstPartOfEmail = "email";
        var expectedMessage = "The user has been saved successfully.";
        var expectedURL = REDIRECT_API_V1_USERS + "/1?sortField=id&sortDir=asc&keyword=" + firstPartOfEmail;


        willDoNothing().given(service).createNewUser(multipart, user);
        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        given(user.getEmail()).willReturn(firstPartOfEmail);

        // when
        var url = controller.createNewUser(multipart, user, attributes);

        // then
        assertEquals(expectedURL, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(service).createNewUser(multipart, user);
    }

    @Test
    void canViewEditUserPage_Success() {
        // given
        var expectedUser = mock(User.class);
        var expectedRoles = List.of(new Role());
        var expectedPageTitle = "Edit User (ID: 0 )";
        var expectedModuleURL = "/api/v1/users";
        var expectedViewName = "users/user_form";

        given(service.findById(anyLong())).willReturn(expectedUser);
        given(service.findAllRoles()).willReturn(expectedRoles);
        given(model.getAttribute("user")).willReturn(expectedUser);
        given(model.getAttribute("roles")).willReturn(expectedRoles);
        given(model.getAttribute("pageTitle")).willReturn(expectedPageTitle);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var viewName = controller.viewEditUserPage(anyLong(), model, attributes);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedUser, model.getAttribute("user"));
        assertEquals(expectedRoles, model.getAttribute("roles"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
    }

    @Test
    void canViewEditUserPage_Fail() {
        // given
        given(service.findById(anyLong())).willThrow(UserNotFoundException.class);

        // when
        var url = controller.viewEditUserPage(anyLong(), model, attributes);

        // then
        assertThrows(UserNotFoundException.class, () -> service.findById(anyLong()));
        assertEquals(REDIRECT_API_V1_USERS, url);
    }

    @Test
    void canDeleteUser_Success() {
        // given
        var userId = 1L;
        var expectedMessage = "The user with ID " + userId + " has been deleted successfully";

        willDoNothing().given(service).delete(userId);
        given(attributes.getAttribute("message")).willReturn(expectedMessage);

        // when
        var url = controller.deleteUser(userId, attributes);

        // then
        verify(service).delete(userId);
        assertEquals(REDIRECT_API_V1_USERS, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
    }

    @Test
    void canDeleteUser_Fail() {
        // given
        var userId = 1L;
        var expectedMessage = "Could not find the user with id " + userId;

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willThrow(UserNotFoundException.class).given(service).delete(userId);

        // when
        var url = controller.deleteUser(userId, attributes);

        // then
        assertEquals(REDIRECT_API_V1_USERS, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        assertThrows(UserNotFoundException.class, () -> service.delete(userId));
    }

    @Test
    void canChangeUserStateToEnabled() {
        // given
        var userId = 1L;
        var expectedMessage = "User with ID: " + userId + " is now Enabled.";

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(service).changeUserState(userId, true);

        // when
        var url = controller.changeUserStateToEnabled(userId, attributes);

        // then
        assertEquals(REDIRECT_API_V1_USERS, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
    }

    @Test
    void canChangeUserStateToDisabled() {
        // given
        var userId = 1L;
        var expectedMessage = "User with ID: " + userId + " is now Disabled.";

        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(service).changeUserState(userId, false);

        // when
        var url = controller.changeUserStateToDisabled(userId, attributes);

        // then
        assertEquals(REDIRECT_API_V1_USERS, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
    }
}
