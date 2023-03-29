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

    public List<Setting> getGeneralSettings() {
        return  (List<Setting>) repository.findByCategory(SettingCategory.GENERAL);
    }

    public GeneralSettingBag getAllSettings() {
        var general = (List<Setting>) repository.findByCategory(SettingCategory.GENERAL);
        var currency = (List<Setting>) repository.findByCategory(SettingCategory.CURRENCY);

        var settings = new ArrayList<>(general);
        settings.addAll(currency);

        return new GeneralSettingBag(settings);
    }

    @Transactional
    public void saveAll(Iterable<Setting> settings) {
        repository.saveAll(settings);
    }
}
