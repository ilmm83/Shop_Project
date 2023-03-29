package com.shop.admin.repository.setting;

import com.shop.model.Setting;
import com.shop.model.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback(true)
class SettingRepositoryTest {

    @Autowired
    private SettingRepository repository;


    @Test
    void canCreateGeneralSettings() {
        var site_name = new Setting("SITE_NAME", "Shop", SettingCategory.GENERAL);
        var site_logo = new Setting("SITE_LOGO", "Shop.png", SettingCategory.GENERAL);
        var copyright = new Setting("COPYRIGHT", "Copyright (C) 2023 Shop Ltd.", SettingCategory.GENERAL);
        var saved = (ArrayList) repository.saveAll(List.of(site_logo, site_name, copyright));

        assertThat(saved).isNotNull();
        assertThat(saved.size()).isEqualTo(3);
    }

    @Test
    void canCreateCurrencySettings() {
        var currencyId = new Setting("CURRENCY_ID", "1", SettingCategory.CURRENCY);
        var currencySymbol = new Setting("CURRENCY_SYMBOL", "$", SettingCategory.CURRENCY);
        var symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION", "before", SettingCategory.CURRENCY);
        var decimalPointType = new Setting("DECIMAL_POINT_TYPE", "POINT", SettingCategory.CURRENCY);
        var decimalDigits = new Setting("DECIMAL_DIGITS", "2", SettingCategory.CURRENCY);
        var thousandsPointType = new Setting("THOUSANDS_POINT_TYPE", "COMMA", SettingCategory.CURRENCY);

        var saved = (ArrayList) repository.saveAll(
                List.of(currencyId, currencySymbol, symbolPosition, decimalPointType, thousandsPointType, decimalDigits));

        assertThat(saved).isNotNull();
        assertThat(saved.size()).isEqualTo(6);
    }

    @Test
    void canFindByCategory() {
        var found = (ArrayList) repository.findByCategory(SettingCategory.CURRENCY);

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(6);
    }
}