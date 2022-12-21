package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.SearchUserDTO;
import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.model.User;
import com.example.adminservice.service.UserService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> create(@Valid @RequestBody UserRequestDTO userRequestDTO,
                                                 @CurrentUser CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã tạp User thành công!",
                        Constants.RESPONSE_CODE.CREATED,
                        userService.createUser(customUserDetails, userRequestDTO))
        );
    }

    @PostMapping("/getAllUsers")
    public ResponseEntity<ResponseObject> getAllUsers(@CurrentUser CustomUserDetails customUserDetails,
                                                      @RequestBody SearchUserDTO searchUserDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Tìm kiếm thành công!", Constants.RESPONSE_CODE.OK,
                        userService.findAll(customUserDetails, searchUserDTO))
        );
    }
}
