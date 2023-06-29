package controller;

import com.common.model.Customer;
import com.shop.admin.customer.CustomerController;
import com.shop.admin.customer.CustomerNotFoundException;
import com.shop.admin.customer.CustomerService;
import com.shop.admin.paging.PagingAndSortingHelper;
import com.shop.admin.utils.exporter.CustomerCSVExporter;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    CustomerService service;

    @Mock
    Model model;

    CustomerController controller;


    @BeforeEach
    void setup() {
        controller = new CustomerController(service);
    }

    @Test
    void canViewFirstPage() {
        // given
        var expectedResult = "redirect:/api/v1/customers/1?sortField=id&sortDir=asc";

        // when
        var result = controller.viewFirstPage();

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canViewPage() {
        // given
        var helper = mock(PagingAndSortingHelper.class);

        willDoNothing().given(service).listByPage(1, helper);

        // when
        controller.viewByPage(helper, 1);

        // then
        verify(service, times(1)).listByPage(1, helper);
    }

    @Test
    void canEnableCustomer() {
        // given
        var expectedResult = "redirect:/api/v1/customers";
        var expectedMessage = "Customer with ID: 1 is enabled.";

        willDoNothing().given(service).enable(1, true);
        given(model.getAttribute("message")).willReturn(expectedMessage);

        // when
        var result = controller.enableCustomer(1, model);

        // then
        verify(service, times(1)).enable(1, true);
        assertEquals(expectedMessage, model.getAttribute("message"));
        assertEquals(expectedResult, result);
    }

    @Test
    void canDisableCustomer() {
        // given
        var expectedResult = "redirect:/api/v1/customers";
        var expectedMessage = "Customer with ID: 1 is disabled.";

        willDoNothing().given(service).enable(1, false);
        given(model.getAttribute("message")).willReturn(expectedMessage);

        // when
        var result = controller.disableCustomer(1, model);

        // then
        verify(service, times(1)).enable(1, false);
        assertEquals(expectedMessage, model.getAttribute("message"));
        assertEquals(expectedResult, result);
    }

    @Test
    void canExportToCSV() throws IOException {
        // given
        var customer = new Customer();
        var expectedUsers = List.of(customer);
        var exporter = mock(CustomerCSVExporter.class);
        var response = mock(HttpServletResponse.class);
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
    void canViewCustomerDetails() {
        // given
        var customer = new Customer();
        var expectedResult = "customers/customer_detail_modal";

        given(service.findById(1L)).willReturn(customer);
        given(model.getAttribute("customer")).willReturn(customer);

        // when
        var result = controller.viewCustomerDetails(1L, model);

        // then
        assertEquals(expectedResult, result);
        assertEquals(customer, model.getAttribute("customer"));
    }

    @Test
    void canViewEditCustomerPage() {
        // given
        var customer = new Customer();
        var expectedResult = "customers/customer_edit_form";
        var expectedPageTitle = "Edit Customer";
        var expectedModuleURL = "/api/v1/customers";

        given(service.findById(1L)).willReturn(customer);
        given(model.getAttribute("customer")).willReturn(customer);
        given(model.getAttribute("pageTitle")).willReturn(expectedPageTitle);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var result = controller.viewEditCustomerPage(1L, model);

        // then
        assertEquals(expectedResult, result);
        assertEquals(customer, model.getAttribute("customer"));
        assertEquals(expectedPageTitle, model.getAttribute("pageTitle"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
    }

    @Test
    void canDeleteCustomer_Success() {
        // given
        var customerId = 1L;
        var expectedMessage = "The customer with ID: " + customerId + " has deleted successfully.";
        var expectedResult = "redirect:/api/v1/customers";

        willDoNothing().given(service).deleteById(customerId);
        given(model.getAttribute("message")).willReturn(expectedMessage);

        // when
        var result = controller.deleteCustomer(customerId, model);

        // then
        assertEquals(expectedResult, result);
        assertEquals(expectedMessage, model.getAttribute("message"));
    }

    @Test
    void canDeleteCustomer_Fail() {
        // given
        var customerId = 1L;
        var expectedResult = "redirect:/api/v1/customers";

        willThrow(CustomerNotFoundException.class).given(service).deleteById(customerId);

        // when
        var result = controller.deleteCustomer(customerId, model);

        // then
        assertEquals(expectedResult, result);
        assertThrows(CustomerNotFoundException.class, () -> service.deleteById(customerId));
    }

    @Test
    void canUpdateCustomer() {
        // given
        var updatedCustomer = new Customer();
        var expectedResult = "redirect:/api/v1/customers";

        willDoNothing().given(service).update(updatedCustomer);

        // when
        var result = controller.updateCustomer(updatedCustomer);

        // then
        assertEquals(expectedResult, result);
    }
}
