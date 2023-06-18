package com.shop.site.repository;

import com.common.model.Setting;
import com.common.model.SettingCategory;
import com.shop.site.setting.SettingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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
        // given
        var settingCategoryGeneral = SettingCategory.GENERAL;
        var settingCategoryCurrency = SettingCategory.CURRENCY;
        var expectedResult = List.of(new Setting());

        willReturn(expectedResult).given(repository).findByTwoCategories(settingCategoryGeneral, settingCategoryCurrency);

        // when
        var result = (List<Setting>) repository.findByTwoCategories(settingCategoryGeneral, settingCategoryCurrency);

        // then
        assertEquals(expectedResult, result);
        assertEquals(expectedResult.get(0), result.get(0));
    }
}
