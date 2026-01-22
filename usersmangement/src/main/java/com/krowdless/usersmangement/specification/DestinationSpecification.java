package com.krowdless.usersmangement.specification;

import com.krowdless.usersmangement.entity.Destination;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class DestinationSpecification {

    public static Specification<Destination> filter(
            Long stateId,
            String name
    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // 🔒 only active destinations
            predicates.add(cb.isTrue(root.get("active")));

            if (stateId != null) {
                predicates.add(cb.equal(root.get("stateId"), stateId));
            }

            if (name != null && !name.isBlank()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                    )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
