package controller;

import com.shop.admin.product.ProductRestController;
import com.shop.admin.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductRestControllerTest {

    @Mock
    ProductService service;

    ProductRestController controller;


    @BeforeEach
    void setup() {
        controller = new ProductRestController(service);
    }

    @Test
    void canCheckNameAndAliasUnique() {
        // given
        var expectedResult = "OK";
        var productId = 1L;
        var name = "name";
        var alias = "alias";

        given(service.checkNameAndAliasUnique(productId, name, alias)).willReturn(expectedResult);

        // when
        var result = controller.checkNameAndAliasUnique(productId, name, alias);

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canRemoveProductImage() {
        // given
        var productId = 1L;
        var fileName = "name";

        willDoNothing().given(service).removeImage(productId, fileName);

        // when
        controller.removeProductImage(productId, fileName);

        // then
        verify(service).removeImage(productId, fileName);
    }
}
