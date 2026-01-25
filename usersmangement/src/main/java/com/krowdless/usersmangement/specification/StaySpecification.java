package com.krowdless.usersmangement.specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.krowdless.usersmangement.entity.Stay;

import jakarta.persistence.criteria.Predicate;

public class StaySpecification {

    public static Specification<Stay> filter(
            Long stateId,
            String propertyType,
            Integer guests,
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // only ACTIVE stays
            predicates.add(cb.equal(root.get("status"), "ACTIVE"));

            if (stateId != null) {
                predicates.add(cb.equal(root.get("stateId"), stateId));
            }

            if (propertyType != null && !propertyType.isBlank()) {
                predicates.add(cb.equal(root.get("propertyType"), propertyType));
            }

            if (guests != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("maxGuests"), guests));
            }

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("pricePerNight"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("pricePerNight"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
