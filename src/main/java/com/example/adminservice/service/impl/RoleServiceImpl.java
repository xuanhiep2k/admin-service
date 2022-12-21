package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.RoleDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Partner;
import com.example.adminservice.model.Role;
import com.example.adminservice.model.RoleFunction;
import com.example.adminservice.repository.PartnerRepository;
import com.example.adminservice.repository.RoleFunctionRepository;
import com.example.adminservice.repository.RoleRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.RoleService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PartnerRepository partnerRepository;
    private final RoleFunctionRepository roleFunctionRepository;
    private final ActionLogService actionLogService;

    @Override
    public RoleDTO createRole(CustomUserDetails customUserDetails, RoleDTO roleDTO) {
        if (roleDTO.getCode().equals(Constants.ROLE.ADMIN)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, "Mã quyền không hợp lệ");
        }
        int limit = -1;
        if (!customUserDetails.getRoles().contains(Constants.ROLE.ADMIN)) {
            Partner partner = partnerRepository.findByCode(customUserDetails.getPartnerCode());
            limit = partner.getSizeRole();
        }
        if (limit >= 0) {
            int currentSize = roleRepository.countByPartnerCode(customUserDetails.getPartnerCode());
            if (currentSize >= limit) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Số lượng quyền có thể quản lý đạt giới hạn");
            }
        }
        Role role = roleRepository.findByCode(roleDTO.getCode());
        if (role == null) {
            add(customUserDetails, roleDTO);
            actionLogService.createLog(
                    customUserDetails, Constants.ACTION.CREATE, Constants.TITLE_LOG.ROLE,
                    MessageFormat.format("Đã tạo quyền {0} thành công", roleDTO.getCode()));
        } else {
            if (!role.getStatus().equals(Constants.STATUS.ACTIVE)) {
                throw new ServerException(ErrorCode.BAD_REQUEST,
                        MessageFormat.format("Quyền {0} đang ở trạng thái khóa", role.getName()));
            } else {
                throw new ServerException(ErrorCode.BAD_REQUEST,
                        MessageFormat.format("Mã quyền {0} đã tồn tại", role.getCode()));
            }
        }
        return roleDTO;
    }

    @Override
    public RoleDTO updateRole(CustomUserDetails customUserDetails, RoleDTO roleDTO) {
        Role roleOld = roleRepository.findByCode(roleDTO.getCode());
        if (roleOld == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, "Quyền không tồn tại");
        } else {
            if (!customUserDetails.getRoles().contains(Constants.ROLE.ADMIN)
                    && !roleOld.getPartnerCode().equals(customUserDetails.getPartnerCode())) {
                throw new ServerException(ErrorCode.BAD_REQUEST, "Không có quyền sửa");
            }
            roleOld.setName(roleDTO.getName());
            roleOld.setDescription(roleDTO.getDescription());
            roleOld.setUpdatedBy(customUserDetails.getUsername());
            roleOld.setUpdatedDate(new Date());

            update(roleOld, roleDTO);
            actionLogService.createLog(
                    customUserDetails, Constants.ACTION.UPDATE, Constants.TITLE_LOG.ROLE,
                    MessageFormat.format("Đã cập nhật quyền {0} thành công", roleDTO.getCode()));
            return roleDTO;
        }
    }

    @Transactional
    public void add(CustomUserDetails currentUser, RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setPartnerCode(currentUser.getPartnerCode());
        role.setCreatedBy(currentUser.getUsername());
        role.setCreatedDate(new Date());
        role.setStatus(Constants.STATUS.ACTIVE);
        Role result = roleRepository.saveAndFlush(role);
        List<RoleFunction> listRF = new ArrayList<>();
        List<String> functionCodeList = roleDTO.getFunctionCodes();
        if (functionCodeList != null && functionCodeList.size() > 0) {
            for (String code : functionCodeList) {
                RoleFunction roleFunction = new RoleFunction(result.getCode(), code);
                roleFunction.setCreatedBy(currentUser.getUsername());
                roleFunction.setCreatedDate(new Date());
                roleFunction.setStatus(Constants.STATUS.ACTIVE);
                listRF.add(roleFunction);
            }
        }
        roleFunctionRepository.saveAll(listRF);
    }

    @Transactional
    public void update(Role role, RoleDTO roleDTO) {
        List<RoleFunction> listRF = roleFunctionRepository.findAllByRoleCode(role.getCode());
        List<RoleFunction> add = new ArrayList<>();
        List<RoleFunction> delete = new ArrayList<>();
        List<String> codes = roleDTO.getFunctionCodes();
        for (RoleFunction rf : listRF) {
            if (codes.contains(rf.getFunctionCode())) {
                codes.remove(rf.getFunctionCode());
            } else {
                delete.add(rf);
            }
        }
        for (String codeF : codes) {
            RoleFunction roleFunction = new RoleFunction(role.getCode(), codeF);
            roleFunction.setCreatedBy(role.getUpdatedBy());
            roleFunction.setCreatedDate(new Date());
            roleFunction.setStatus(Constants.STATUS.ACTIVE);
            add.add(roleFunction);
        }
        if (delete.size() > 0) {
            roleFunctionRepository.deleteAll(delete);
        }
        if (add.size() > 0) {
            roleFunctionRepository.saveAll(add);
        }
        roleRepository.save(role);
    }
}
