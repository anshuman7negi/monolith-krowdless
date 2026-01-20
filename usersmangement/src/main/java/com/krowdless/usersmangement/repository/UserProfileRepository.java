package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.krowdless.usersmangement.entity.UserProfileEntity;

public interface UserProfileRepository
        extends JpaRepository<UserProfileEntity, Long> {
}
