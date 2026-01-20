package com.krowdless.usersmangement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.krowdless.usersmangement.entity.TitleEntity;

public interface TitleRepository extends JpaRepository<TitleEntity, Long> {
}
