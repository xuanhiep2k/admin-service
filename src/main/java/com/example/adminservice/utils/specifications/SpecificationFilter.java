package com.example.adminservice.utils.specifications;

import com.example.adminservice.dto.*;
import com.example.adminservice.model.*;
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

    public static Specification<Function> specificationFunction(SearchFunctionDTO searchFunctionDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            if (searchFunctionDTO.getCode() != null && !searchFunctionDTO.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + searchFunctionDTO.getCode() + "%"));
            }

            if (searchFunctionDTO.getName() != null && !searchFunctionDTO.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchFunctionDTO.getName() + "%"));
            }

            if (searchFunctionDTO.getStatus() != null && !searchFunctionDTO.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + searchFunctionDTO.getStatus() + "%"));
            }

            if (searchFunctionDTO.getType() != null && !searchFunctionDTO.getType().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("type"), "%" + searchFunctionDTO.getType() + "%"));
            }

            if (searchFunctionDTO.getAppCode() != null && !searchFunctionDTO.getAppCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("appCode"), "%" + searchFunctionDTO.getAppCode() + "%"));
            }

            if (searchFunctionDTO.getParentCode() != null && !searchFunctionDTO.getParentCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("parentCode"), "%" + searchFunctionDTO.getParentCode() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<App> specificationApp(SearchAppDTO searchAppDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            if (searchAppDTO.getCode() != null && !searchAppDTO.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + searchAppDTO.getCode() + "%"));
            }

            if (searchAppDTO.getName() != null && !searchAppDTO.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchAppDTO.getName() + "%"));
            }

            if (searchAppDTO.getStatus() != null && !searchAppDTO.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + searchAppDTO.getStatus() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Role> specificationRole(SearchRoleDTO searchRoleDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            if (searchRoleDTO.getCode() != null && !searchRoleDTO.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + searchRoleDTO.getCode() + "%"));
            }

            if (searchRoleDTO.getName() != null && !searchRoleDTO.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchRoleDTO.getName() + "%"));
            }

            if (searchRoleDTO.getStatus() != null && !searchRoleDTO.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + searchRoleDTO.getStatus() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Department> specificationDepartment(SearchDepartmentDTO searchDepartmentDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            query.distinct(true);
            if (searchDepartmentDTO.getCode() != null && !searchDepartmentDTO.getCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("code"), "%" + searchDepartmentDTO.getCode() + "%"));
            }

            if (searchDepartmentDTO.getName() != null && !searchDepartmentDTO.getName().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchDepartmentDTO.getName() + "%"));
            }

            if (searchDepartmentDTO.getStatus() != null && !searchDepartmentDTO.getStatus().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("status"), "%" + searchDepartmentDTO.getStatus() + "%"));
            }

            if (searchDepartmentDTO.getPartnerCode() != null && !searchDepartmentDTO.getPartnerCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("partnerCode"), "%" + searchDepartmentDTO.getPartnerCode() + "%"));
            }

            if (searchDepartmentDTO.getParentCode() != null && !searchDepartmentDTO.getParentCode().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("parentCode]"), "%" + searchDepartmentDTO.getParentCode() + "%"));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
