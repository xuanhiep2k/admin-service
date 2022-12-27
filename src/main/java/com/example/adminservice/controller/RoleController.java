package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.RoleDTO;
import com.example.adminservice.dto.SearchFunctionDTO;
import com.example.adminservice.dto.SearchRoleDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.RoleService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/getAllRoles")
    public ResponseEntity<ResponseObject> getAllRoles(@CurrentUser CustomUserDetails customUserDetails,
                                                      @Valid @RequestBody SearchRoleDTO searchRoleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Tìm kiếm role thành công!", Constants.RESPONSE_CODE.OK,
                        roleService.findAll(customUserDetails, searchRoleDTO))
        );
    }

    @PostMapping("/lock/{code}")
    public ResponseEntity<ResponseObject> lockRole(@CurrentUser CustomUserDetails customUserDetails,
                                                   @PathVariable String code) {
        roleService.lockRole(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Khoá role thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }

    @PostMapping("/unlock/{code}")
    public ResponseEntity<ResponseObject> unlockRole(@CurrentUser CustomUserDetails customUserDetails,
                                                     @PathVariable String code) {
        roleService.unlockRole(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Mở khoá role thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }

    @PostMapping("/delete/{code}")
    public ResponseEntity<ResponseObject> deleteRole(@CurrentUser CustomUserDetails customUserDetails,
                                                     @PathVariable String code) {
        roleService.deleteRole(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Xoá role thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }
}
