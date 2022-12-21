package com.example.adminservice.service;

import com.example.adminservice.dto.FunctionDTO;
import com.example.adminservice.model.CustomUserDetails;

public interface FunctionService {
    FunctionDTO createFunction(CustomUserDetails customUserDetails, FunctionDTO functionDTO);
}
