package com.example.adminservice.service;

import com.example.adminservice.dto.SearchUserDTO;
import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.User;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO findByUsernameIgnoreCaseAndStatus(String username);

    UserDTO createUser(CustomUserDetails customUserDetails, UserRequestDTO userRequestDTO);

    Page<User> findAll(CustomUserDetails customUserDetails, SearchUserDTO searchUserDTO);
}
