package com.shop.admin.user;

import com.common.model.User;
import com.shop.admin.paging.SearchRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends SearchRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<Long> countById(Long id);

    Optional<User> findById(Long id);

    User save(User user);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    void updateEnabledStatus(Long id, boolean enabled);

    void deleteById(Long id);

    List<User> findAll();
}
