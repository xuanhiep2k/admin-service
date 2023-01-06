package com.example.adminservice.service.impl;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.dto.RoleDTO;
import com.example.adminservice.dto.SearchRoleDTO;
import com.example.adminservice.dto.TreeNodeDTO;
import com.example.adminservice.dto.response.RoleResponseDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.*;
import com.example.adminservice.repository.FunctionRepository;
import com.example.adminservice.repository.PartnerRepository;
import com.example.adminservice.repository.RoleFunctionRepository;
import com.example.adminservice.repository.RoleRepository;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.RoleService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import com.example.adminservice.utils.specifications.SpecificationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PartnerRepository partnerRepository;
    private final RoleFunctionRepository roleFunctionRepository;
    private final FunctionRepository functionRepository;
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
            roleOld.setPartnerCode(roleDTO.getPartnerCode());
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

    @Override
    public Page<RoleResponseDTO> findAll(CustomUserDetails customUserDetails, SearchRoleDTO searchRoleDTO) {
        Sort sort = Sort.by(searchRoleDTO.getSortDirection(), searchRoleDTO.getSortBy());
        Pageable pageable;
        if (searchRoleDTO.getPageSize() == 0) {
            pageable = PageRequest.of(searchRoleDTO.getPageNumber(), Integer.MAX_VALUE, sort);
        } else {
            pageable = PageRequest.of(searchRoleDTO.getPageNumber(), searchRoleDTO.getPageSize(), sort);
        }
        Specification<Role> specification = SpecificationFilter.specificationRole(searchRoleDTO);
        List<Role> roles = roleRepository.findAll(specification);
        List<RoleResponseDTO> roleResponseDTOs = new ArrayList<>();
        for (Role role : roles) {
            RoleResponseDTO roleResponseDTO = new RoleResponseDTO();
            BeanUtils.copyProperties(role, roleResponseDTO);
            List<Function> list =
                    functionRepository.getFunctionActiveByRole(role.getCode(), Constants.STATUS.ACTIVE);
            roleResponseDTO.setFunctionNames(
                    list.stream().map(Function::getName).collect(Collectors.toList()));
            roleResponseDTO.setFunctionCodes(
                    list.stream().map(Function::getCode).collect(Collectors.toList()));
            List<TreeNodeDTO> tree = DataUtil.buildTreeSelection(list);
            roleResponseDTO.setFunctions(tree);
            roleResponseDTOs.add(roleResponseDTO);
        }

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), roleResponseDTOs.size());
        Page<RoleResponseDTO> page;
        if (start > roleResponseDTOs.size()) {
            page = new PageImpl<>(new ArrayList<>(), pageable, roleResponseDTOs.size());
        } else {
            page = new PageImpl<>(roleResponseDTOs.subList(start, end), pageable, roleResponseDTOs.size());
        }

        actionLogService.createLog(customUserDetails, Constants.ACTION.SEARCH, Constants.TITLE_LOG.ROLE,
                MessageFormat.format("Đã tìm kiếm Role", searchRoleDTO.toString()));
        return page;
    }

    @Transactional
    public void add(CustomUserDetails currentUser, RoleDTO roleDTO) {
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setPartnerCode(roleDTO.getPartnerCode());
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
    @CacheEvict(value = Constants.CACHE.FUNCTION_OF_ROLE, key = "#role.code")
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

    @Override
    public void lockRole(CustomUserDetails customUserDetails, String code) {
        Role role = roleRepository.findByCode(code);
        if (role == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("Role {0} không tồn tại", code));
        } else if (!role.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("Role {0} đang bị khoá", code));
        } else {
            role.setStatus(Constants.STATUS.LOCKED);
            roleRepository.save(role);
            actionLogService.createLog(customUserDetails, Constants.ACTION.LOCK, Constants.TITLE_LOG.ROLE,
                    MessageFormat.format("Khoá quyền {0} thành công", code));
        }
    }

    @Override
    public void unlockRole(CustomUserDetails customUserDetails, String code) {
        Role role = roleRepository.findByCode(code);
        if (role == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("Role {0} không tồn tại", code));
        } else if (role.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("Role {0} phải được khoá trước", code));
        } else {
            role.setStatus(Constants.STATUS.ACTIVE);
            roleRepository.save(role);
            actionLogService.createLog(customUserDetails, Constants.ACTION.UNLOCK, Constants.TITLE_LOG.ROLE,
                    MessageFormat.format("Mở khoá quyền {0} thành công", code));
        }
    }

    @Override
    public void deleteRole(CustomUserDetails customUserDetails, String code) {
        Role role = roleRepository.findByCode(code);
        if (role == null) {
            throw new ServerException(ErrorCode.NOT_FOUND, MessageFormat.format("Role {0} không tồn tại", code));
        } else if (role.getStatus().equals(Constants.STATUS.ACTIVE)) {
            throw new ServerException(ErrorCode.BAD_REQUEST, MessageFormat.format("Role {0} phải được khoá trước", code));
        } else {
            roleRepository.delete(role);
            actionLogService.createLog(customUserDetails, Constants.ACTION.DELETE, Constants.TITLE_LOG.ROLE,
                    MessageFormat.format("Xoá quyền {0} thành công", code));
        }
    }

    @Override
    public List<Function> listFunctionByUser(List<String> role, String appCode, String type) {
        if (!StringUtils.isEmpty(appCode)) {
            if (role.contains(Constants.ROLE.ADMIN)) {
                return functionRepository.findAllByStatusAndAppCodeAndType(
                        Constants.STATUS.ACTIVE, appCode, type);
            } else {
                return functionRepository.getFunctionActiveByUser(role, Constants.STATUS.ACTIVE, appCode);
            }
        } else {
            return new ArrayList<>();
        }
    }
}
