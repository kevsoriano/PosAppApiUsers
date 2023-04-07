package com.jkngil.pos.users.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long>{
	UserEntity findByUserId(String userId);
	UserEntity findByEmail(String email);
}
