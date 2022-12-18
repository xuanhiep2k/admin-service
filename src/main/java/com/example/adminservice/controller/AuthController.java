package com.example.adminservice.controller;

import com.example.adminservice.config.security.JwtTokenUtil;
import com.example.adminservice.dto.UserDTO;
import com.example.adminservice.dto.request.LoginRequestDTO;
import com.example.adminservice.model.CustomUserDetails;
import com.example.adminservice.model.ResponseObject;
import com.example.adminservice.service.ActionLogService;
import com.example.adminservice.service.UserService;
import com.example.adminservice.utils.Constants;
import com.example.adminservice.utils.DataUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final ActionLogService actionLogService;

    @PostMapping("/login")
    public ResponseEntity<ResponseObject> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        principal.eraseCredentials();

        UserDTO userDTO =
                userService.findByUsernameIgnoreCaseAndStatus(principal.getUsername());
        if (userDTO == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ResponseObject("Tài khoản không tồn tại hoặc bị khóa", Constants.RESPONSE_CODE.BAD_REQUEST, null));
        } else {
            userDTO.setPassword("");
            userDTO.setAccessToken(jwtTokenUtil.generateToken(principal));
//            actionLogService.addLog(principal, Constants.TITLE_LOG.LOGIN, "Đăng nhập thành công");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Đăng nhập thành công", Constants.RESPONSE_CODE.OK, userDTO));
        }
    }
}
