package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.RoleDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.RoleService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createRole(@CurrentUser CustomUserDetails customUserDetails,
                                                     @Valid @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã tạo quyền thành công", Constants.RESPONSE_CODE.CREATED,
                        roleService.createRole(customUserDetails, roleDTO))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateRole(@CurrentUser CustomUserDetails customUserDetails,
                                                     @Valid @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã cập nhật quyền thành công", Constants.RESPONSE_CODE.OK,
                        roleService.updateRole(customUserDetails, roleDTO))
        );
    }
}
