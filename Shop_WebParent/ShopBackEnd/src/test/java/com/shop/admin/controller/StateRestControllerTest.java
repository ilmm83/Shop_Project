package com.shop.admin.controller;

import com.common.model.Country;
import com.common.model.State;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.admin.state.StateRestController;
import com.shop.admin.state.StateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class StateRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    StateService service;

    StateRestController controller;


    @BeforeEach
    void setup() {
        controller = new StateRestController(service);
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void canViewAllByCountry() throws Exception {
        // given
        var country = new Country(1);
        var expectedStates = List.of(new State(1, "name", country));

        given(service.listByCountry(any(Country.class))).willReturn(expectedStates);

        // when and then
        mockMvc.perform(get("/api/v1/states/list/{country_id}", country.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void canSaveState() throws Exception {
        // given
        var country = new Country(1);
        var state = new State(1, "name", country);

        given(service.save(any(State.class))).willReturn(state.getId());

        // when and then
        mockMvc.perform(post("/api/v1/states/save")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(state)))
            .andExpect(status().isCreated())
            .andExpect(content().json(state.getId().toString()));
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void canUpdateState() throws Exception {
        // given
        var country = new Country(1);
        var state = new State(1, "name", country);

        given(service.update(anyInt(), any(State.class))).willReturn(state.getId());

        // when and then
        mockMvc.perform(put("/api/v1/states/update/{state_id}", state.getId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(state)))
            .andExpect(status().isOk())
            .andExpect(content().json(state.getId().toString()));
    }

    @Test
    @WithMockUser(authorities = "Admin")
    void canDeleteState() throws Exception {
        // given
        var stateId = 1;

        given(service.delete(anyInt())).willReturn(stateId);

        // when and then
        mockMvc.perform(delete("/api/v1/states/delete/{state_id}", stateId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(String.valueOf(stateId)));
    }
}
