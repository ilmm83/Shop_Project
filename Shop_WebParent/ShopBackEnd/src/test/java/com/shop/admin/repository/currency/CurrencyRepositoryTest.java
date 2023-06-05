package com.shop.admin.repository.currency;


import com.shop.admin.currency.CurrencyRepository;
import com.common.model.Currency;
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
class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository repository;

    @Test
    void canCreateCurrencies() {
        var currencies = List.of(
                 new Currency("United States Dollar", "$", "USD")
                ,new Currency("Australian Dollar", "$", "AUD")
                ,new Currency("Canadian Dollar", "$", "CAD")
                ,new Currency("Brazilian Real", "R$", "BRL")
                ,new Currency("Japanese Yen", "¥", "JPY")
                ,new Currency("Chinese Yuan", "¥", "CNY")
                ,new Currency("Russian Ruble", "₽", "RUB")
                ,new Currency("British Pounds", "£", "GBP")
                ,new Currency("South Korean Won", "₩", "KRW")
                ,new Currency("Vietnamese Dong", "₫", "VND")
                ,new Currency("Indian Rupee", "₹", "INR")
                ,new Currency("Euro", "€", "EUR")
        );
        var saved = (ArrayList) repository.saveAll(currencies);

        assertThat(saved).isNotNull();
        assertThat(saved.size()).isEqualTo(12);
    }

    @Test
    void canFindAllByOrderByNameAsc() {
        var found = (ArrayList) repository.findAllByOrderByIdAsc();

        assertThat(found).isNotNull();
        assertThat(found.size()).isEqualTo(12);
    }
}