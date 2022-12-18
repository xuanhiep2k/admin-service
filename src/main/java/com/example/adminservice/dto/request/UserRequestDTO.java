package com.example.adminservice.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserRequestDTO {

    @NotNull(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải có độ dài từ 3 -> 50")
    private String username;

    @NotNull(message = "Mật khẩu không được để trống")
    @Size(min = 7, max = 20, message = "Mật khẩu phải có độ dài từ 7 -> 20")
    private String password;

    @NotNull(message = "Email không được để trống")
    @Size(min = 5, max = 50, message = "Email phải có độ dài từ 5 -> 100 ")
    @Email
    private String email;

    private String phone;

    @NotEmpty(message = "Quyền không được để trống")
    private List<String> roles;

    private String status;

    @NotNull(message = "Tên không được để trống")
    @Size(min = 3, max = 100, message = "Tên phải có độ dài từ 3 -> 100 ")
    private String fullName;

    private String departmentCode;
    private String partnerCode;
}
