package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.DepartmentDTO;
import com.example.adminservice.dto.SearchDepartmentDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.DepartmentService;
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
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping("/create")
    ResponseEntity<ResponseObject> createDepartment(@CurrentUser CustomUserDetails customUserDetails,
                                                    @Valid @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã tạo department thành công!", Constants.RESPONSE_CODE.CREATED,
                        departmentService.createDepartment(customUserDetails, departmentDTO))
        );
    }

    @PostMapping("/getAllDepartments")
    ResponseEntity<ResponseObject> getAllDepartments(@CurrentUser CustomUserDetails customUserDetails,
                                                     @Valid @RequestBody SearchDepartmentDTO searchDepartmentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã tìm kiếm department thành công!", Constants.RESPONSE_CODE.OK,
                        departmentService.findAll(customUserDetails, searchDepartmentDTO))
        );
    }

    @PostMapping("/getAllByPartnerCode")
    ResponseEntity<ResponseObject> getAllByPartnerCode(@CurrentUser CustomUserDetails customUserDetails,
                                                       @Valid @RequestBody SearchDepartmentDTO searchDepartmentDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã tìm kiếm department thành công!", Constants.RESPONSE_CODE.OK,
                        departmentService.findAllByPartnerCode(customUserDetails, searchDepartmentDTO.getPartnerCode()))
        );
    }
}
