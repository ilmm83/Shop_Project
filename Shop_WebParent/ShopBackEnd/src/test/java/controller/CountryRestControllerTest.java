package controller;

import com.common.model.Country;
import com.shop.admin.country.CountryNotFoundException;
import com.shop.admin.country.CountryRestController;
import com.shop.admin.country.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CountryRestControllerTest {

    @Mock
    CountryService service;

    CountryRestController controller;


    @BeforeEach
    void setup() {
        controller = new CountryRestController(service);
    }

    @Test
    void canListAllCountries() {
        // given
        var expectedCountries = List.of(new Country());
        var expectedResult = ResponseEntity.ok(expectedCountries);

        given(service.listAllCountriesOrderByNameAsc()).willReturn(expectedCountries);

        // when
        var result = controller.listAllCountries();

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void canSaveCountry() {
        // given
        var expectedCountry = new Country(1, "name", "code");
        var expectedResult = new ResponseEntity<>(1, HttpStatus.CREATED);

        given(service.save(expectedCountry)).willReturn(expectedCountry.getId());

        // when
        var result = controller.save(expectedCountry);

        // then
        assertEquals(expectedResult, result);
        assertEquals(expectedResult.getStatusCode(), result.getStatusCode());
    }

    @Test
    void canUpdateCountry() {
        // given
        var countryId = 1;
        var updatedCountry = new Country(1, "Name", "code");
        var expectedResult = new ResponseEntity<>(countryId, HttpStatus.CREATED);

        given(service.update(countryId, updatedCountry)).willReturn(countryId);

        // when
        var result = controller.update(countryId, updatedCountry);

        // then
        assertEquals(expectedResult, result);
        assertEquals(expectedResult.getStatusCode(), result.getStatusCode());
    }

    @Test
    void canDeleteCountry() {
        // given
        var countryId = 1;
        var expectedResult = new ResponseEntity<>(countryId, HttpStatus.OK);

        given(service.delete(countryId)).willReturn(countryId);

        // when
        var result = controller.delete(countryId);

        // then
        assertEquals(expectedResult, result);
        assertEquals(expectedResult.getStatusCode(), result.getStatusCode());
    }

    @Test
    void canHandleAuthRegisterException() {
        // given
        var exception = mock(CountryNotFoundException.class);

        given(exception.getMessage()).willReturn("Country not found");

        // when
        var result = controller.handleAuthRegisterException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(result.getBody().message(), exception.getMessage());
    }
}
