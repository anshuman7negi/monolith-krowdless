package com.krowdless.usersmangement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.krowdless.usersmangement.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);

    // @Query("""
    // SELECT new com.krowdless.usersmangement.dto.TopTravelerDto(
    //     u.id,
    //     u.username,
    //     u.profileImageUrl,
    //     urs.totalPoints,
    //     urs.totalTrips,
    //     urs.totalSpent,
    //     t.name,
    //     t.icon,
    //     COUNT(f.id)
    // )
    // FROM UserEntity u
    // JOIN UserRewardsSummaryEntity urs ON urs.userId = u.id
    // LEFT JOIN UserTitleEntity ut ON ut.userId = u.id
    // LEFT JOIN TitleEntity t ON t.id = ut.titleId
    // LEFT JOIN FollowerEntity f ON f.followingId = u.id
    // GROUP BY
    //     u.id, u.username, u.profileImageUrl,
    //     urs.totalPoints, urs.totalTrips, urs.totalSpent,
    //     t.name, t.icon
    // ORDER BY
    //     urs.totalPoints DESC,
    //     urs.totalTrips DESC,
    //     urs.totalSpent DESC,
    //     u.id ASC
    // """)
    // Page<TopTravelerDto> findTopTravelers(Pageable pageable);
}
