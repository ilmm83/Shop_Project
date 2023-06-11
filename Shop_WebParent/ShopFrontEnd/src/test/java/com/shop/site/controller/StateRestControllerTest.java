package com.shop.site.controller;

import com.common.dto.StateDTO;
import com.common.model.Country;
import com.common.model.State;
import com.shop.site.state.StateRepository;
import com.shop.site.state.StateRestController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class StateRestControllerTest {

    @Spy
    StateRepository repository;


    @Test
    void testListAllByCountry() {
        // given
        var countryId = 1;
        var country = new Country(countryId);
        var states = List.of(new State("State 1", country));
        var expectedDtos = List.of(convertToStateDTO(new State("State 1", country)));
        var expectedResponse = ResponseEntity.ok(expectedDtos);

        // when
        doReturn(states).when(repository).findByCountryOrderByNameAsc(any(Country.class));

        var controller = new StateRestController(repository);
        var actualResponse = controller.listAllByCountry(1);

        // then
        assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
        assertEquals(expectedDtos.size(), actualResponse.getBody().size());

        var expectedDto = expectedDtos.get(0);
        var actualDto = actualResponse.getBody().get(0);
        assertEquals(expectedDto.getId(), actualDto.getId());
        assertEquals(expectedDto.getName(), actualDto.getName());
    }

    private StateDTO convertToStateDTO(State state) {
        var dto = new StateDTO();
        dto.setId(state.getId());
        dto.setName(state.getName());

        return dto;
    }
}