package com.example.adminservice.service;

import com.example.adminservice.dto.RoleDTO;
import com.example.adminservice.dto.SearchRoleDTO;
import com.example.adminservice.dto.response.RoleResponseDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Function;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoleService {
    RoleDTO createRole(CustomUserDetails customUserDetails, RoleDTO roleDTO);

    RoleDTO updateRole(CustomUserDetails customUserDetails, RoleDTO roleDTO);

    Page<RoleResponseDTO> findAll(CustomUserDetails customUserDetails, SearchRoleDTO searchRoleDTO);

    void lockRole(CustomUserDetails customUserDetails, String code);

    void unlockRole(CustomUserDetails customUserDetails, String code);

    void deleteRole(CustomUserDetails customUserDetails, String code);

    List<Function> listFunctionByUser(List<String> role, String appCode, String name);
}
