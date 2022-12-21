package com.example.adminservice.controller;

import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.PartnerDTO;
import com.example.adminservice.dto.SearchPartnerDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.PageModel;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.PartnerService;
import com.example.adminservice.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> createPartner(@CurrentUser CustomUserDetails customUserDetails,
                                                        @Valid @RequestBody PartnerDTO partnerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã tạo partner thành công!", Constants.RESPONSE_CODE.CREATED,
                        partnerService.createPartner(customUserDetails, partnerDTO))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> updatePartner(@CurrentUser CustomUserDetails customUserDetails,
                                                        @Valid @RequestBody PartnerDTO partnerDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã cập nhật partner thành công!", Constants.RESPONSE_CODE.CREATED,
                        partnerService.updatePartner(customUserDetails, partnerDTO))
        );
    }

    @PostMapping("/getAllPartners")
    public ResponseEntity<ResponseObject> getAllPartners(@CurrentUser CustomUserDetails customUserDetails,
                                                         @Valid @RequestBody SearchPartnerDTO searchPartnerDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Tìm kiếm partner thành công!", Constants.RESPONSE_CODE.OK,
                        partnerService.findAll(customUserDetails, searchPartnerDTO))
        );
    }

    @PostMapping("/lockPartner/{code}")
    public ResponseEntity<ResponseObject> lockPartner(@CurrentUser CustomUserDetails customUserDetails,
                                                      @PathVariable String code) {
        partnerService.lockPartner(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Khoá partner thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }

    @PostMapping("/unlockPartner/{code}")
    public ResponseEntity<ResponseObject> unlockPartner(@CurrentUser CustomUserDetails customUserDetails,
                                                      @PathVariable String code) {
        partnerService.unlockPartner(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Khoá partner thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }
    @PostMapping("/deletePartner/{code}")
    public ResponseEntity<ResponseObject> deletePartner(@CurrentUser CustomUserDetails customUserDetails,
                                                        @PathVariable String code) {
        partnerService.deletePartner(customUserDetails, code);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Xoá partner thành công!", Constants.RESPONSE_CODE.OK, "")
        );
    }
}
