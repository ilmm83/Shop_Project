package com.shop.admin.controller;

import com.common.model.Country;
import com.common.model.Currency;
import com.common.model.Setting;
import com.shop.admin.country.CountryService;
import com.shop.admin.currency.CurrencyService;
import com.shop.admin.setting.GeneralSettingBag;
import com.shop.admin.setting.SettingController;
import com.shop.admin.setting.SettingService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SettingControllerTest {

    @Mock
    SettingService settingService;

    @Mock
    CurrencyService currencyService;

    @Mock
    CountryService countryService;

    @Mock
    HttpServletRequest request;

    @Mock
    RedirectAttributes attributes;

    SettingController controller;


    @BeforeEach
    void setup() {
        controller = new SettingController(settingService, currencyService, countryService);
    }

    @Test
    void canViewAllSettings() {
        // given
        var model = mock(Model.class);
        var expectedCurrencies = List.of(new Currency());
        var expectedCountries = List.of(new Country());
        var expectedModuleURL = "/api/v1/settings";
        var expectedViewName = "settings/settings";

        given(settingService.findAllSettings()).willReturn(List.of(new Setting()));
        given(currencyService.findAllByOrderByIdAsc()).willReturn(expectedCurrencies);
        given(countryService.listAllCountriesOrderByNameAsc()).willReturn(expectedCountries);
        given(model.getAttribute("currencies")).willReturn(expectedCurrencies);
        given(model.getAttribute("countries")).willReturn(expectedCountries);
        given(model.getAttribute("moduleURL")).willReturn(expectedModuleURL);

        // when
        var viewName = controller.viewAllSettings(model);

        // then
        assertEquals(expectedViewName, viewName);
        assertEquals(expectedCurrencies, model.getAttribute("currencies"));
        assertEquals(expectedCountries, model.getAttribute("countries"));
        assertEquals(expectedModuleURL, model.getAttribute("moduleURL"));
    }

    @Test
    void canSaveGeneralSettings() {
        // given
        var settings = List.of(new Setting());
        var multipart = mock(MultipartFile.class);
        var settingBag = new GeneralSettingBag(settings);
        var expectedURL = "redirect:/api/v1/settings";
        var expectedMessage = "General settings have been saved.";

        willDoNothing().given(settingService).saveLogo(multipart, settingBag);
        willDoNothing().given(settingService).saveCurrencySymbol(request, settingBag, currencyService);
        willDoNothing().given(settingService).updateSettingValues(request, settings);
        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        given(settingService.getGeneralSettings()).willReturn(settingBag);

        // when
        var url = controller.saveGeneralSettings(multipart, request, attributes);

        // then
        assertEquals(expectedURL, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
        verify(settingService).saveLogo(multipart, settingBag);
        verify(settingService).saveCurrencySymbol(request, settingBag, currencyService);
        verify(settingService).updateSettingValues(request, settings);
    }

    @Test
    void canSaveMailServerSettings() {
        // given
        var settings = List.of(new Setting());
        var settingBag = new GeneralSettingBag(settings);
        var expectedMessage = "Mail Server settings have been saved.";
        var expectedURL = "redirect:/api/v1/settings";

        given(settingService.getMainServerSettings()).willReturn(settingBag);
        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(settingService).updateSettingValues(request, settingBag.getSettings());

        // when
        var url = controller.saveMailServerSettings(attributes, request);

        // then
        assertEquals(expectedURL, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
    }

    @Test
    void canSaveMailTemplateSettings() {
        // given
        var settings = List.of(new Setting());
        var settingBag = new GeneralSettingBag(settings);
        var expectedMessage = "Mail Templates settings have been saved.";
        var expectedURL = "redirect:/api/v1/settings";

        given(settingService.getMainTemplatesSettings()).willReturn(settingBag);
        given(attributes.getAttribute("message")).willReturn(expectedMessage);
        willDoNothing().given(settingService).updateSettingValues(request, settingBag.getSettings());

        // when
        var url = controller.saveMailTemplatesSettings(attributes, request);

        // then
        assertEquals(expectedURL, url);
        assertEquals(expectedMessage, attributes.getAttribute("message"));
    }
}
