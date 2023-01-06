package com.example.adminservice.service;

import com.example.adminservice.dto.FunctionDTO;
import com.example.adminservice.dto.SearchFunctionDTO;
import com.example.adminservice.dto.TreeNodeDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.Function;
import com.example.adminservice.model.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FunctionService {
    FunctionDTO createFunction(CustomUserDetails customUserDetails, FunctionDTO functionDTO);


    Page<TreeNodeDTO> getTree(CustomUserDetails customUserDetails, SearchFunctionDTO searchFunctionDTO);

    Page<Function> findAll(CustomUserDetails customUserDetails, SearchFunctionDTO searchFunctionDTO);

    FunctionDTO updateFunction(CustomUserDetails customUserDetails, FunctionDTO functionDTO);

    void lockFunction(CustomUserDetails customUserDetails, String code);

    void unlockFunction(CustomUserDetails customUserDetails, String code);

    void deleteFunction(CustomUserDetails customUserDetails, String code);

    List<Function> getFunctionByRoleCode(CustomUserDetails customUserDetails, String[] roles);
}
