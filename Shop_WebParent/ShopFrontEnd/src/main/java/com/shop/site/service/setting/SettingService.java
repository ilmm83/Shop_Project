package com.shop.site.service.setting;

import com.shop.model.Setting;
import com.shop.model.SettingCategory;
import com.shop.site.repository.customer.EmailSettingBag;
import com.shop.site.repository.setting.SettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettingService {

    private final SettingRepository repository;


    public List<Setting> getGeneralAndCurrencySettings() {
        var general = (List<Setting>) repository.findByCategory(SettingCategory.GENERAL);
        var currency = (List<Setting>) repository.findByCategory(SettingCategory.CURRENCY);

        var settings = new ArrayList<>(general);
        settings.addAll(currency);

        return settings;
    }

    public EmailSettingBag getEmailSettings() {
        return new EmailSettingBag((List<Setting>) repository.findByTwoCategories(SettingCategory.MAIL_SERVER, SettingCategory.MAIL_TEMPLATES));
    }
}
