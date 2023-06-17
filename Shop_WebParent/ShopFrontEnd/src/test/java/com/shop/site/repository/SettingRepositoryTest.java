package com.shop.site.repository;

import com.common.model.Setting;
import com.common.model.SettingCategory;
import com.shop.site.setting.SettingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.willReturn;

@ExtendWith(MockitoExtension.class)
class SettingRepositoryTest {

    @Spy
    SettingRepository repository;


    @Test
    void canFindByCategory() {
        // given
        var settingCategory = SettingCategory.GENERAL;
        var expectedResult = List.of(new Setting());

        willReturn(expectedResult).given(repository).findByCategory(settingCategory);

        // when
        var result = (List<Setting>) repository.findByCategory(settingCategory);

        // then
        assertEquals(expectedResult, result);
        assertEquals(expectedResult.get(0), result.get(0));
    }

    @Test
    void canFindByTwoCategories() {
        var found = (List<Setting>) repository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.PAYMENT);

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(3);
    }
}
