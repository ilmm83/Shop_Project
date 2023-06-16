package com.shop.site.controller;

import com.shop.site.customer.CustomerRestController;
import com.shop.site.customer.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;


public class CustomerRestControllerTest {

    @Mock
    CustomerService customerService;

    CustomerRestController controller;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new CustomerRestController(customerService);
    }

    @Test
    void isDuplicatedEmail_Success() {
        // given
        var id = 1L;
        var email = "test@email.com";
        var expectedResult = "OK";

        given(customerService.isEmailUnique(id, email)).willReturn(true);

        // when
        var result = controller.isDuplicatedEmail(id, email);

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void isDuplicatedEmail_Fail() {
        // given
        var id = 1L;
        var email = "test@email.com";
        var expectedResult = "Duplicated";

        given(customerService.isEmailUnique(id, email)).willReturn(false);

        // when
        var result = controller.isDuplicatedEmail(id, email);

        // then
        assertEquals(expectedResult, result);
    }

}
