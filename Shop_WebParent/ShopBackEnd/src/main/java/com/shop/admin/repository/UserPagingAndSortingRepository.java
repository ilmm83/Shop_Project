package com.shop.admin.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.shop.model.User;

@Repository
public interface UserPagingAndSortingRepository extends PagingAndSortingRepository<User, Long>{
  
}
