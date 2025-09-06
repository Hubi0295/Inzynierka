package com.example.auth.entity;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserSpecification {
    public static Specification<User> filtersByParams(Map<String,String> filters){
        return (root,query,criteriaBuilder)->{
            List<Predicate> predicates = new ArrayList<>();
            filters.forEach((key,value)->{
                if (value != null && !value.isEmpty()) {
                    switch (key) {
                        case "uuid" -> predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("uuid").as(String.class)), value.toLowerCase() + "%"
                        ));
                        case "name" -> predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")), value.toLowerCase() + "%"
                        ));
                        case "surname" -> predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("surname")), value.toLowerCase() + "%"
                        ));
                        case "username" -> predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("username")), value.toLowerCase() + "%"
                        ));
                        case "email" -> predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("email")), value.toLowerCase() + "%"
                        ));
                        case "userType" -> predicates.add(criteriaBuilder.equal(
                                root.get("role"), UserType.valueOf(value.toUpperCase())
                        ));
                        case "enabled" -> predicates.add(criteriaBuilder.equal(
                                root.get("isEnable"), Boolean.valueOf(value)
                        ));
                        case "lock" -> predicates.add(criteriaBuilder.equal(
                                root.get("isLock"), Boolean.valueOf(value)
                        ));
                    }
                }
            });
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

