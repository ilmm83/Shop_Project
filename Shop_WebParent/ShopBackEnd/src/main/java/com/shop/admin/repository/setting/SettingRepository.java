package com.shop.admin.repository.setting;

import com.shop.model.Setting;
import com.shop.model.SettingCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {

    Iterable<Setting> findByCategory(SettingCategory category);
}
