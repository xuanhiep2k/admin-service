package com.example.adminservice.service;

import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;

public interface UserService {
    UserDTO findByUsernameIgnoreCaseAndStatus(String username);

    UserDTO createUser(CustomUserDetails customUserDetails, UserRequestDTO userRequestDTO);
}
