package com.shop.dto;

import com.shop.model.Setting;
import lombok.Getter;

import java.util.List;

@Getter
public class SettingBag {

    public SettingBag(List<Setting> settings) {
        this.settings = settings;
    }

    private List<Setting> settings;

    public Setting get(String key) {
        int index = settings.indexOf(new Setting(key));
        if (index >= 0) return settings.get(index);
        return null;
    }

    public String getValue(String key) {
       var setting = get(key);
       return setting == null ? null : setting.getValue();
    }

    public void update(String key, String value) {
        var setting = get(key);
        if (setting == null || value == null) return;

        setting.setKey(key);
        setting.setValue(value);
    }
}
