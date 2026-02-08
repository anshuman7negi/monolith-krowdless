package com.krowdless.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.krowdless.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	boolean existsByEmail(String lowerCase);

	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByPhone(String identifier);

    
}
