package com.example.adminservice.service;

import com.example.adminservice.dto.DepartmentDTO;
import com.example.adminservice.dto.SearchDepartmentDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Department;
import org.springframework.data.domain.Page;

import java.util.List;

public interface DepartmentService {
    Department getByCode(String code, String partnerCode);

    DepartmentDTO createDepartment(CustomUserDetails customUserDetails, DepartmentDTO departmentDTO);

    Page<Department> findAll(CustomUserDetails customUserDetails, SearchDepartmentDTO searchDepartmentDTO);

    List<Department> findAllByPartnerCode(CustomUserDetails customUserDetails, String partnerCode);
}
