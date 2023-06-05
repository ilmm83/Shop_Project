package com.shop.site.repository.setting;

import com.common.model.Setting;
import com.common.model.SettingCategory;
import com.shop.site.setting.SettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback
class SettingRepositoryTest {

    @Autowired
    private SettingRepository repository;

    @Test
    void canFindByCategory() {
        var found = (List<Setting>) repository.findByCategory(SettingCategory.CURRENCY);

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(6);
    }

    @Test
    void canFindByTwoCategories() {
        var found = (List<Setting>) repository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.PAYMENT);

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(3);
    }
}
