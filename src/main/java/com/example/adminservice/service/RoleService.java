package com.example.adminservice.service;

import com.example.adminservice.dto.RoleDTO;
import com.example.adminservice.model.CustomUserDetails;

public interface RoleService {
    RoleDTO createRole(CustomUserDetails customUserDetails, RoleDTO roleDTO);

    RoleDTO updateRole(CustomUserDetails customUserDetails, RoleDTO roleDTO);
}
