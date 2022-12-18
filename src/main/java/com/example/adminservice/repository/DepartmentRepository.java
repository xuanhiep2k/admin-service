package com.example.adminservice.repository;

import com.example.adminservice.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findAllByCodeAndPartnerCode(String code, String partnerCode);
}
