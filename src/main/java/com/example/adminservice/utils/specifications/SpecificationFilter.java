package com.example.adminservice.utils.specifications;

import com.example.adminservice.dto.SearchPartnerDTO;
import com.example.adminservice.dto.SearchUserDTO;
import com.example.adminservice.model.Partner;
import com.example.adminservice.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class SpecificationFilter {
    public static Specification<User> specificationUser(SearchUserDTO searchUserDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            if (searchUserDTO.getUsername() != null && !searchUserDTO.getUsername().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + searchUserDTO.getUsername() + "%"));
            }

            if (searchUserDTO.getFullName() != null && !searchUserDTO.getFullName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + searchUserDTO.getFullName() + "%"));
            }

            if (searchUserDTO.getEmail() != null && !searchUserDTO.getEmail().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + searchUserDTO.getEmail() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Partner> specificationPartner(SearchPartnerDTO searchPartnerDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            if (searchPartnerDTO.getCode() != null && !searchPartnerDTO.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + searchPartnerDTO.getCode() + "%"));
            }

            if (searchPartnerDTO.getName() != null && !searchPartnerDTO.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchPartnerDTO.getName() + "%"));
            }

            if (searchPartnerDTO.getStatus() != null && !searchPartnerDTO.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + searchPartnerDTO.getStatus() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
