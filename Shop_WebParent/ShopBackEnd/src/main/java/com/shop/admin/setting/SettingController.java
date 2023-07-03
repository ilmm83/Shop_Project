package com.shop.admin.setting;

import com.shop.admin.country.CountryService;
import com.shop.admin.currency.CurrencyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("api/v1/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;
    private final CurrencyService currencyService;
    private final CountryService countryService;


    @GetMapping
    public String viewAllSettings(Model model) {
        model.addAttribute("currencies", currencyService.findAllByOrderByIdAsc());
        model.addAttribute("countries", countryService.listAllCountriesOrderByNameAsc());
        model.addAttribute("moduleURL", "/api/v1/settings");

        settingService.findAllSettings()
            .forEach(setting -> model.addAttribute(setting.getKey(), setting.getValue()));

        return "settings/settings";
    }

    @PostMapping("/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipart,
                                      HttpServletRequest request,
                                      RedirectAttributes attributes) {

        var settingBag = settingService.getGeneralSettings();

        settingService.saveLogo(multipart, settingBag);
        settingService.saveCurrencySymbol(request, settingBag, currencyService);
        settingService.updateSettingValues(request, settingBag.getSettings());

        attributes.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/api/v1/settings";
    }

    @PostMapping("/save_mail_server")
    public String saveMailServerSettings(RedirectAttributes attributes, HttpServletRequest request) {
        var settingBag = settingService.getMainServerSettings();
        settingService.updateSettingValues(request, settingBag.getSettings());

        attributes.addFlashAttribute("message", "Mail Server settings have been saved.");

        return "redirect:/api/v1/settings";
    }

    @PostMapping("/save_mail_templates")
    public String saveMailTemplatesSettings(RedirectAttributes attributes, HttpServletRequest request) {
        var settingBag = settingService.getMainTemplatesSettings();
        settingService.updateSettingValues(request, settingBag.getSettings());

        attributes.addFlashAttribute("message", "Mail Templates settings have been saved.");

        return "redirect:/api/v1/settings";
    }
}
