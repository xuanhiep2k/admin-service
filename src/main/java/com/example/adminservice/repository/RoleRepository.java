package com.example.adminservice.repository;

import com.example.adminservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Integer countByPartnerCode(String partnerCode);

    Role findByCode(String code);
}
