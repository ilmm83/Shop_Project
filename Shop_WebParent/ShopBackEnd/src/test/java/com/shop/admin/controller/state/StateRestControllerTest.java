package com.shop.admin.controller.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.model.Country;
import com.shop.model.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class StateRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    private State state;

    @BeforeEach
    void setup() {
        var id = 10;
        state = new State(id, "test_" + id, new Country(21));
    }

    @Test
    @WithMockUser(username = "redsantosph@gmail.com", password = "12345678", roles = "Admin")
    void canListAllByCountry() throws Exception {
        // when
        var response = mvc.perform(get("/api/v1/states/list/{country_id}", state.getCountry().getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    @WithMockUser(username = "redsantosph@gmail.com", password = "12345678", roles = "Admin")
    void canSave() throws Exception {
        // when
        var response = mvc.perform(post("/api/v1/states/save")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(state))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isEqualTo(state.getId().toString());

    }

    @Test
    @WithMockUser(username = "redsantosph@gmail.com", password = "12345678", roles = "Admin")
    void canDelete() throws Exception {
        // when
        var response = mvc.perform(
                        delete("/api/v1/states/delete/{id}", state.getId())
                                .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isEqualTo(state.getId().toString());
    }

}