package com.example.product.entity;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductSpecification {

    public static Specification<Product> filterByParams(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((key, value) -> {
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "uuid" -> predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("uuid").as(String.class)), "%" + value.toLowerCase() + "%"));
                        case "rfid" -> predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("rfid")), value.toLowerCase() + "%"));
                        case "name" -> predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), value.toLowerCase() + "%"));
                        case "category" -> {
                            Join<Object, Object> categoryJoin = root.join("category", JoinType.LEFT);
                            predicates.add(criteriaBuilder.like(
                                    criteriaBuilder.lower(categoryJoin.get("name")),
                                    value.toLowerCase() + "%"
                            ));
                        }
                        case "spot" -> predicates.add(criteriaBuilder.equal(root.get("spot"), Integer.valueOf(value)));
                        case "contractor" -> predicates.add(criteriaBuilder.equal(root.get("contractor"), Integer.valueOf(value)));
                        case "updated_at" -> predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("updated_at").as(String.class)), value.toLowerCase() + "%"));
                    }
                }
            });

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
