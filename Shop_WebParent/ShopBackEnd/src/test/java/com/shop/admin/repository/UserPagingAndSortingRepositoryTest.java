package com.shop.admin.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.shop.model.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserPagingAndSortingRepositoryTest {

  @Autowired
  private UserPagingAndSortingRepository repository;


  @Test
  public void canListFirstPageOfUsers() {
    int pageNumber = 0, pageSize = 4; 
    
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<User> findAll = repository.findAll(pageable);
    var list = findAll.getContent();

    list.forEach(System.out::println);

    assertThat(list).hasSize(pageSize);
  }
}
