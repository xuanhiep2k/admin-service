package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.DepartmentDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Department;
import com.example.adminservice.repository.DepartmentRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.DepartmentService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ActionLogService actionLogService;

    @Override
    public Department getByCode(String code, String partnerCode) {
        return departmentRepository.findAllByCodeAndPartnerCode(code, partnerCode);
    }

    @Override
    public DepartmentDTO createDepartment(CustomUserDetails customUserDetails, DepartmentDTO departmentDTO) {
        if (customUserDetails.getRoles().contains(Constants.ROLE.ADMIN)
                || customUserDetails.getPartnerCode().equals(departmentDTO.getPartnerCode())) {
            Department department = departmentRepository.findAllByCodeAndPartnerCode(departmentDTO.getCode(), departmentDTO.getPartnerCode());
            if (department == null) {
                String path = "";
                if (!DataUtil.isNullOrEmpty(departmentDTO.getParentCode())) {
                    Department departmentParent = departmentRepository.findAllByCodeAndPartnerCode(departmentDTO.getParentCode(), departmentDTO.getPartnerCode());
                    if (departmentParent != null) {
                        if (!departmentParent.getStatus().equals(Constants.STATUS.ACTIVE)) {
                            throw new ServerException(ErrorCode.BAD_REQUEST, "Phòng/ban cha không hoạt động");
                        }
                        path = departmentParent.getPath();
                    } else {
                        throw new ServerException(ErrorCode.NOT_FOUND,
                                MessageFormat.format("Phòng/ban cha {0} không tồn tại", departmentDTO.getCode()));
                    }
                }
                path += ("/" + departmentDTO.getCode());
                department = new Department();
                BeanUtils.copyProperties(departmentDTO, department);
                department.setCreatedBy(customUserDetails.getUsername());
                department.setCreatedDate(new Date());
                department.setStatus(Constants.STATUS.ACTIVE);
                department.setPath(path);
                departmentRepository.saveAndFlush(department);
                actionLogService.createLog(
                        customUserDetails, Constants.ACTION.CREATE, Constants.TITLE_LOG.DEPARTMENT,
                        MessageFormat.format("Đã tạo phòng/ban {0} thành công!", departmentDTO.getName()));
            }
        }
        return departmentDTO;
    }
}