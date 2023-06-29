package controller;

import com.shop.admin.customer.CustomerRestController;
import com.shop.admin.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CustomerRestControllerTest {

    @Mock
    CustomerService service;

    CustomerRestController controller;


    @BeforeEach
    void setup() {
        controller = new CustomerRestController(service);
    }

    @Test
    void canCheckIsDuplicatedEmail_OK() {
        // given
        var customerId = 1L;
        var email = "email";
        var expectedResult = "OK";

        given(service.isEmailUnique(customerId, email)).willReturn(true);

        // when
        var result = controller.checkIsDuplicatedEmail(customerId, email);

        // then
        assertEquals(expectedResult, result);
        verify(service).isEmailUnique(customerId, email);
    }

    @Test
    void canCheckIsDuplicatedEmail_Duplicated() {
        // given
        var customerId = 1L;
        var email = "email";
        var expectedResult = "Duplicated";

        given(service.isEmailUnique(customerId, email)).willReturn(false);

        // when
        var result = controller.checkIsDuplicatedEmail(customerId, email);

        // then
        assertEquals(expectedResult, result);
        verify(service).isEmailUnique(customerId, email);
    }
}
