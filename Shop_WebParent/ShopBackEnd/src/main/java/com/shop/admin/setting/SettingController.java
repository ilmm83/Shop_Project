package com.shop.admin.setting;

import com.shop.admin.currency.CurrencyNotFound;
import com.shop.admin.country.CountryService;
import com.shop.admin.currency.CurrencyService;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.model.Setting;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

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

    @SneakyThrows
    @PostMapping("/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipart, HttpServletRequest request,
                                      RedirectAttributes attributes) {
        var settingBag = settingService.getGeneralSettings();
        saveLogo(multipart, settingBag);
        saveCurrencySymbol(request, settingBag);
        updateSettingValues(request, settingBag.getSettings());
        attributes.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/api/v1/settings";
    }

    @PostMapping("/save_mail_server")
    public String saveMailServerSettings(RedirectAttributes attributes, HttpServletRequest request) {
        var settingBag = settingService.getMainServerSettings();
        updateSettingValues(request, settingBag.getSettings());
        attributes.addFlashAttribute("message", "Mail Server settings have been saved.");

        return "redirect:/api/v1/settings";
    }

    @PostMapping("/save_mail_templates")
    public String saveMailTemplatesSettings(RedirectAttributes attributes, HttpServletRequest request) {
        var settingBag = settingService.getMainTemplatesSettings();
        updateSettingValues(request, settingBag.getSettings());
        attributes.addFlashAttribute("message", "Mail Templates settings have been saved.");

        return "redirect:/api/v1/settings";
    }

    private void updateSettingValues(HttpServletRequest request, List<Setting> settings) {
        for (var setting : settings) {
            var value = request.getParameter(setting.getKey());
            if (value == null) continue;
            setting.setValue(value);
        }
        settingService.saveAll(settings);
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) throws CurrencyNotFound {
        var curIndex = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        var currency = currencyService.findById(curIndex);
        settingBag.updateCurrencySymbol(currency.getSymbol());
    }

    private void saveLogo(MultipartFile multipart, GeneralSettingBag settingBag) throws IOException {
        if (!multipart.isEmpty()) {
            var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
            var uploadDir = "./Shop_WebParent/site-logo/";
            FileUploadUtil.saveFile(uploadDir, fileName, multipart);
            settingBag.updateSiteLogo("/site-logo/" + fileName);
        }
    }

}
