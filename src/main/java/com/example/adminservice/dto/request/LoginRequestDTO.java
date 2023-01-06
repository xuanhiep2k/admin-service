package com.example.adminservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginRequestDTO {
    @NotNull(message = "Tên đăng nhập không được để trống")
    @Size(min = 1, max = 50, message = "Tên đăng nhập phải có độ từ dài từ 3 -> 50 kí tự")
    private String username;

    @NotNull(message = "Mật khẩu không được để trống")
    @Size(min = 1, max = 20, message = "Mật khẩu phải có độ từ dài từ 1 -> 20 kí tự")
    private String password;

    private String app;
}
