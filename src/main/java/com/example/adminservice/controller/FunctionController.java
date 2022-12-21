package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.FunctionDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.FunctionService;
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
@RequestMapping("/function")
@RequiredArgsConstructor
public class FunctionController {
    private final FunctionService functionService;

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createFunction(@CurrentUser CustomUserDetails customUserDetails,
                                                         @Valid @RequestBody FunctionDTO functionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã tạo chức năng thành công", Constants.RESPONSE_CODE.CREATED,
                        functionService.createFunction(customUserDetails, functionDTO))
        );
    }
}
