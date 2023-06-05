package com.shop.admin.brand.controller.country;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.common.model.Country;
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
class CountryRestControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;



    @Test
    @WithMockUser(username = "redsantosph@gmail.com", password = "12345678", roles = "Admin")
    void canListAllCountries() throws Exception {
        // when
        var response = mvc.perform(get("/api/v1/countries/list"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    @WithMockUser(username = "redsantosph@gmail.com", password = "12345678", roles = "Admin")
    void canSaveCountry() throws Exception {
        var country = new Country(2, "C", "CCC");

        // when
        var response = mvc.perform(post("/api/v1/countries/save")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(country))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isEqualTo(String.valueOf(country.getId()));
    }

    @Test
    @WithMockUser(username = "redsantosph@gmail.com", password = "12345678", roles = "Admin")
    void canUpdateCountry() throws Exception {
        var country = new Country(2, "C2", "CCC");

        // when
        var response = mvc.perform(post("/api/v1/countries/save")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(country))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        // then
        assertThat(response.getResponse().getContentAsString()).isEqualTo(String.valueOf(country.getId()));
    }

    @Test
    @WithMockUser(username = "redsantosph@gmail.com", password = "12345678", roles = "Admin")
    void canDeleteCountry() throws Exception {
        mvc.perform(delete("/api/v1/countries/delete/2")
                        .contentType("application/json")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
    }

}