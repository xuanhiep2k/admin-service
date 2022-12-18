package com.example.adminservice.service.impl;

import com.example.adminservice.model.Department;
import com.example.adminservice.repository.DepartmentRepository;
import com.example.adminservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public Department getByCode(String code, String partnerCode) {
        return departmentRepository.findAllByCodeAndPartnerCode(code, partnerCode);
    }
}
