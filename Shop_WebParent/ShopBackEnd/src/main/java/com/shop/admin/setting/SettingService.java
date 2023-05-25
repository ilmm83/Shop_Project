package com.shop.admin.setting;

import com.shop.admin.currency.CurrencyService;
import com.shop.admin.utils.FileNotSavedException;
import com.shop.admin.utils.FileUploadUtil;
import com.shop.model.Setting;
import com.shop.model.SettingCategory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettingService {

    private final SettingRepository repository;


    public List<Setting> findAllSettings() {
        return (List<Setting>) repository.findAll();
    }

    public GeneralSettingBag getMainServerSettings() {
        return new GeneralSettingBag((List<Setting>) repository.findByCategory(SettingCategory.MAIL_SERVER));
    }

    public GeneralSettingBag getMainTemplatesSettings() {
        return new GeneralSettingBag((List<Setting>) repository.findByCategory(SettingCategory.MAIL_TEMPLATES));
    }

    public GeneralSettingBag getGeneralSettings() {
        var general = (List<Setting>) repository.findByCategory(SettingCategory.GENERAL);
        var currency = (List<Setting>) repository.findByCategory(SettingCategory.CURRENCY);

        general.addAll(currency);
        return new GeneralSettingBag(general);
    }

    public void updateSettingValues(HttpServletRequest request, List<Setting> settings) {
        for (var setting : settings) {
            var value = request.getParameter(setting.getKey());
            if (value == null) continue;
            setting.setValue(value);
        }
        saveAll(settings);
    }

    public void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag, CurrencyService currencyService) {
        var curIndex = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        var currency = currencyService.findById(curIndex);
        settingBag.updateCurrencySymbol(currency.getSymbol());
    }

    public void saveLogo(MultipartFile multipart, GeneralSettingBag settingBag) {
        if (!multipart.isEmpty()) {
            var fileName = StringUtils.cleanPath(multipart.getOriginalFilename());
            var uploadDir = "./Shop_WebParent/site-logo/";
            try {
                FileUploadUtil.saveFile(uploadDir, fileName, multipart);
            } catch (IOException e) {
                throw new FileNotSavedException(e.getMessage(), e);
            }
            settingBag.updateSiteLogo("/site-logo/" + fileName);
        }
    }

    @Transactional
    private void saveAll(Iterable<Setting> settings) {
        repository.saveAll(settings);
    }
}
