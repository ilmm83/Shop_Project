package com.shop.admin.setting;

import com.common.dto.SettingBag;
import com.common.model.Setting;

import java.util.List;

public class GeneralSettingBag extends SettingBag {

    public GeneralSettingBag(List<Setting> settings) {
        super(settings);
    }

    public void updateCurrencySymbol(String value) {
        super.update("CURRENCY_SYMBOL", value);
    }

    public void updateSiteLogo(String value) {
        super.update("SITE_LOGO", value);
    }
}
