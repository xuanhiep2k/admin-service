package com.example.adminservice.repository;

import com.example.adminservice.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Integer countByPartnerCode(String partnerCode);

    Role findByCode(String code);

    List<Role> findAll(Specification<Role> specification);

    Page<Role> findAll(Specification<Role> specification, Pageable pageable);
}
