package com.krowdless.usersmangement.specification;

import com.krowdless.usersmangement.entity.Destination;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.util.ArrayList;
import java.util.List;

public class DestinationSpecification {

    public static Specification<Destination> filter(
            Long stateId,
            String name,
            Long categoryId   // 👈 CATEGORY FILTER
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔒 only active destinations
            predicates.add(cb.isTrue(root.get("active")));

            // state filter
            if (stateId != null) {
                predicates.add(
                        cb.equal(root.get("stateId"), stateId)
                );
            }

            // name search
            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            // 🔥 CATEGORY FILTER (ONLY FILTER, NO RESPONSE)
            if (categoryId != null) {

                Join<Object, Object> dcJoin =
                        root.join("destinationCategories", JoinType.INNER);

                predicates.add(
                        cb.equal(
                                dcJoin.get("id").get("categoryId"),
                                categoryId
                        )
                );

                // ⚠️ IMPORTANT for pagination
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
