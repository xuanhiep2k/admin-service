package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.AppDTO;
import com.example.adminservice.dto.FunctionDTO;
import com.example.adminservice.dto.SearchAppDTO;
import com.example.adminservice.dto.SearchFunctionDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.AppService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppController {
    private final AppService appService;

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createApp(@CurrentUser CustomUserDetails customUserDetails,
                                                    @Valid @RequestBody AppDTO appDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã tạo ứng dụng thành công", Constants.RESPONSE_CODE.CREATED,
                        appService.createApp(customUserDetails, appDTO))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateApp(@CurrentUser CustomUserDetails customUserDetails,
                                                    @Valid @RequestBody AppDTO appDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã cập nhật ứng dụng thành công", Constants.RESPONSE_CODE.OK,
                        appService.updateApp(customUserDetails, appDTO))
        );
    }

    @PostMapping("/getAllApps")
    public ResponseEntity<ResponseObject> getAllApps(@CurrentUser CustomUserDetails customUserDetails,
                                                     @Valid @RequestBody SearchAppDTO searchAppDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Tìm kiếm App thành công!", Constants.RESPONSE_CODE.OK,
                        appService.findAll(customUserDetails, searchAppDTO))
        );
    }

    @PostMapping("/lockApp/{code}")
    public ResponseEntity<ResponseObject> lockApp(@CurrentUser CustomUserDetails customUserDetails,
                                                  @PathVariable String code) {
        appService.lockApp(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Khoá app thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }

    @PostMapping("/unlockApp/{code}")
    public ResponseEntity<ResponseObject> unlockApp(@CurrentUser CustomUserDetails customUserDetails,
                                                    @PathVariable String code) {
        appService.unlockApp(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Mở khoá App thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }

    @PostMapping("/deleteApp/{code}")
    public ResponseEntity<ResponseObject> deleteApp(@CurrentUser CustomUserDetails customUserDetails,
                                                    @PathVariable String code) {
        appService.deleteApp(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Xoá App thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }
}
