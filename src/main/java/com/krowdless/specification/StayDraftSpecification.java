package com.krowdless.specification;

import java.time.OffsetDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.krowdless.entity.StayDraft;

import jakarta.persistence.criteria.Predicate;


public class StayDraftSpecification {

    public static Specification<StayDraft> filter(
            String status,
            OffsetDateTime fromDate,
            OffsetDateTime toDate) {

        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            // 🔹 STATUS (default handled in service)
            if (status != null && !status.isBlank()) {
                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("status"), status));
            }

            // 🔹 FROM DATE
            if (fromDate != null) {
                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"), fromDate));
            }

            // 🔹 TO DATE
            if (toDate != null) {
                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"), toDate));
            }

            return predicate;
        };
    }
}
