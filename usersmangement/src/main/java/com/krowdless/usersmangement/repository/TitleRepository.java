package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.krowdless.usersmangement.entity.TitleEntity;

public interface TitleRepository extends JpaRepository<TitleEntity, Long> {

    // @Query("""
    //     SELECT t
    //     FROM UserTitleEntity ut
    //     JOIN TitleEntity t ON ut.titleId = t.id
    //     WHERE ut.userId = :userId
    //     ORDER BY ut.achievedAt DESC
    // """)
    // List<TitleEntity> findLatestTitle(@Param("userId") Long userId);
}
