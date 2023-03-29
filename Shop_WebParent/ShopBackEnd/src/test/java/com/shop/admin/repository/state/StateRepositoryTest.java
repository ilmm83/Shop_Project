package com.shop.admin.repository.state;

import com.shop.model.Country;
import com.shop.model.State;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = NONE)
@Rollback
class StateRepositoryTest {

    @Autowired
    private StateRepository repository;

    @Autowired
    private TestEntityManager em;

    private Country country;


    @BeforeEach
    void beforeEach() {
        country = em.find(Country.class, 1);
    }

    @Test
    void canCreateStates() {
        var states = (List<State>) repository.saveAll(List.of(
                 new State("Karnataka", country)
                ,new State("Punjab", country)
                ,new State("Uttar Pradesh", country)
                ,new State("West Bengal", country)));

        assertThat(states).isNotNull();
        assertThat(states.size()).isEqualTo(4);
    }

    @Test
    void canReadState() {
        var state = repository.findById(1);

        assertThat(state.isEmpty()).isFalse();
        assertThat(state.get().getName()).isEqualTo("Karnataka");
    }

    @Test
    void canUpdateState() {
        var state = repository.findById(1).get();
        state.setName("State");
        var saved = repository.save(state);

        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("State");
    }

    @Test
    void canDeleteState() {
        repository.deleteById(1);
        var deleted = repository.findById(1);

        assertThat(deleted.isEmpty()).isTrue();
    }

    @Test
    void canFindByCountryOrderByNameAsc() {
        var states = (List<State>) repository.findByCountryOrderByNameAsc(country);

        assertThat(states).isNotNull();
        assertThat(states.size()).isEqualTo(3);
    }
}