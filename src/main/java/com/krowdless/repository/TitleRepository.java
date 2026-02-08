package com.krowdless.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.entity.TitleEntity;

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
