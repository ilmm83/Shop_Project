package com.shop.admin.user;

import com.shop.admin.paging.SearchRepository;
import com.common.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
