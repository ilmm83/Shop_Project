package com.shop.site.setting;

import com.common.model.Setting;
import com.common.model.SettingCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {

    Iterable<Setting> findByCategory(SettingCategory category);

    @Query("SELECT s FROM Setting s WHERE s.category = ?1 OR s.category = ?2")
    Iterable<Setting> findByTwoCategories(SettingCategory f_category, SettingCategory s_category);
}
