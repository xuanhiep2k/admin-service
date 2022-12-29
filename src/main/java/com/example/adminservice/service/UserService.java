package com.example.adminservice.service;

import com.example.adminservice.dto.SearchUserDTO;
import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserDTO findByUsernameIgnoreCaseAndStatus(String username);

    UserDTO updateUser(CustomUserDetails customUserDetails, UserRequestDTO userRequestDTO, MultipartFile multipartFile) throws IOException;

    Page<User> findAll(CustomUserDetails customUserDetails, SearchUserDTO searchUserDTO);

    UserDTO findByUsername(String username);

    UserDTO getCurrentUserInfo(CustomUserDetails customUserDetails);

    UserDTO createUser(CustomUserDetails customUserDetails, UserRequestDTO userRequestDTO, MultipartFile multipartFile) throws IOException;
}
