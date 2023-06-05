package com.shop.admin.setting;

import com.common.model.Setting;
import com.common.model.SettingCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {

    Iterable<Setting> findByCategory(SettingCategory category);
}
