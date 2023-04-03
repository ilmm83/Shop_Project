package com.shop.admin.service.setting;

import com.shop.admin.repository.setting.GeneralSettingBag;
import com.shop.admin.repository.setting.SettingRepository;
import com.shop.model.Setting;
import com.shop.model.SettingCategory;
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

    @Transactional
    public void saveAll(Iterable<Setting> settings) {
        repository.saveAll(settings);
    }
}
