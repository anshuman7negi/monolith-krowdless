package com.krowdless.usersmangement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.krowdless.usersmangement.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Active categories (UI filter ke liye)
    List<Category> findByActiveTrueOrderByNameAsc();
}
