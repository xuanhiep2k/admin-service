package com.example.adminservice.controller;

import com.example.adminservice.config.ErrorCode;
import com.example.adminservice.config.security.CurrentUser;
import com.example.adminservice.dto.SearchUserDTO;
import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.UserRequestDTO;
import com.example.adminservice.exception.ServerException;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.model.User;
import com.example.adminservice.service.UserService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @Value("${spring.servlet.multipart.location}")
    private String UPLOADED_FOLDER;

    @RequestMapping("/me")
    public ResponseEntity<Object> getCurrentUserInfo(@CurrentUser CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã lấy thông tin tài khoản thành công!",
                        Constants.RESPONSE_CODE.OK,
                        userService.getCurrentUserInfo(customUserDetails))
        );
    }

    @RequestMapping("/checkPermission")
    public ResponseEntity<Object> checkPermission(Authentication authentication, Object o, Collection<ConfigAttribute> collection) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> needRoles = collection.stream().map(ConfigAttribute::getAttribute).collect(Collectors.toList());
        for (GrantedAuthority grantedAuthority : authorities) {
            if (needRoles.contains(grantedAuthority.getAuthority())
                    || grantedAuthority.getAuthority().equals(Constants.ROLE.ADMIN)) {
                return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("", Constants.RESPONSE_CODE.OK, ""));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("", Constants.RESPONSE_CODE.FORBIDDEN, ""));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseObject> create(@RequestPart("user") @Valid UserRequestDTO userRequestDTO,
                                                 @CurrentUser CustomUserDetails customUserDetails,
                                                 @RequestParam(value = "avatar", required = false) MultipartFile multipartFile) throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseObject("Đã tạo User thành công!",
                        Constants.RESPONSE_CODE.CREATED,
                        userService.createUser(customUserDetails, userRequestDTO, multipartFile))
        );
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseObject> upadte(@RequestPart("user") @Valid UserRequestDTO userRequestDTO,
                                                 @CurrentUser CustomUserDetails customUserDetails,
                                                 @RequestParam(value = "avatar", required = false) MultipartFile multipartFile) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("Đã cập nhật User thành công!",
                        Constants.RESPONSE_CODE.OK,
                        userService.updateUser(customUserDetails, userRequestDTO, multipartFile))
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

    @RequestMapping(value = "/avatar/{file_name}", method = RequestMethod.GET, produces = MediaType.IMAGE_PNG_VALUE)
    public FileSystemResource getAvatar(@PathVariable("file_name") String fileName) {
        return new FileSystemResource(UPLOADED_FOLDER + "/" + fileName);
    }
}
