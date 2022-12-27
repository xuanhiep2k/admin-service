package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.FunctionDTO;
import com.example.adminservice.dto.SearchFunctionDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.FunctionService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updateFunction(@CurrentUser CustomUserDetails customUserDetails,
                                                         @Valid @RequestBody FunctionDTO functionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã cập nhật chức năng thành công", Constants.RESPONSE_CODE.OK,
                        functionService.updateFunction(customUserDetails, functionDTO))
        );
    }

    @PostMapping("/getAllFunctions")
    public ResponseEntity<ResponseObject> getAllFunctions(@CurrentUser CustomUserDetails customUserDetails,
                                                          @Valid @RequestBody SearchFunctionDTO searchFunctionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Tìm kiếm function thành công!", Constants.RESPONSE_CODE.OK,
                        functionService.findAll(customUserDetails, searchFunctionDTO))
        );
    }

    @PostMapping("/getTree")
    public ResponseEntity<ResponseObject> getTree(@CurrentUser CustomUserDetails customUserDetails,
                                                  @Valid @RequestBody SearchFunctionDTO searchFunctionDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Tìm kiếm tree function thành công!", Constants.RESPONSE_CODE.OK,
                        functionService.getTree(customUserDetails, searchFunctionDTO))
        );
    }

    @PostMapping("/lock/{code}")
    public ResponseEntity<ResponseObject> lockFunction(@CurrentUser CustomUserDetails customUserDetails,
                                                       @PathVariable String code) {
        functionService.lockFunction(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã khoá chức năng thành công", Constants.RESPONSE_CODE.OK, "")
        );
    }

    @PostMapping("/unlock/{code}")
    public ResponseEntity<ResponseObject> unlockFunction(@CurrentUser CustomUserDetails customUserDetails,
                                                         @PathVariable String code) {
        functionService.unlockFunction(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã mở khoá chức năng thành công", Constants.RESPONSE_CODE.OK, "")
        );
    }

    @PostMapping("/delete/{code}")
    public ResponseEntity<ResponseObject> deleteFunction(@CurrentUser CustomUserDetails customUserDetails,
                                                         @PathVariable String code) {
        functionService.deleteFunction(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã xoá chức năng thành công", Constants.RESPONSE_CODE.OK, "")
        );
    }
}
