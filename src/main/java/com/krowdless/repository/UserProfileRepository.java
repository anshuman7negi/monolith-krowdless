package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.UserProfileEntity;

public interface UserProfileRepository
        extends JpaRepository<UserProfileEntity, Long> {
}
