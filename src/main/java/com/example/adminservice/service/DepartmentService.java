package com.example.adminservice.service;

import com.example.adminservice.model.Department;

public interface DepartmentService {
    Department getByCode(String code, String partnerCode);
}
