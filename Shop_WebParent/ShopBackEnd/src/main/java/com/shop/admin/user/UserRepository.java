package com.shop.admin.user;

import java.util.List;
import java.util.Optional;

import com.shop.admin.paging.SearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.User;

@Repository
public interface UserRepository extends SearchRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<Long> countById(Long id);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabledStatus(Long id, boolean enabled);

    Optional<User> findById(Long id);

    User save(User user);

    void deleteById(Long id);

    List<User> findAll();
}
