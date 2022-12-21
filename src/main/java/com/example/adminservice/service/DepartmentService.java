package com.example.adminservice.service;

import com.example.adminservice.dto.DepartmentDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Department;

public interface DepartmentService {
    Department getByCode(String code, String partnerCode);

    DepartmentDTO createDepartment(CustomUserDetails customUserDetails, DepartmentDTO departmentDTO);
}
