package com.example.adminservice.repository;

import com.example.adminservice.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findAllByCodeAndPartnerCode(String code, String partnerCode);

    Page<Department> findAll(Specification<Department> specification, Pageable pageable);

    List<Department> findAllByPartnerCodeAndStatus(String partnerCode, String status);

}
