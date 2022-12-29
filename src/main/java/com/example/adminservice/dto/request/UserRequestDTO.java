package com.example.adminservice.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UserRequestDTO {

    @NotNull(message = "Tên đăng nhập không được để trống")
    @Size(min = 3, max = 50, message = "Tên đăng nhập phải có độ dài từ 3 -> 50")
    private String username;

    private String password;

    @NotNull(message = "Email không được để trống")
    @Size(min = 5, max = 50, message = "Email phải có độ dài từ 5 -> 100 ")
    @Email
    private String email;

    private String phone;
    private String avatar;

    @NotEmpty(message = "Quyền không được để trống")
    private List<String> roles;

    private String status;

    @NotNull(message = "Tên không được để trống")
    @Size(min = 3, max = 100, message = "Tên phải có độ dài từ 3 -> 100 ")
    private String fullName;

    @NotEmpty(message = "Department không được để trống")
    private String departmentCode;

    @NotEmpty(message = "Partner không được để trống")
    private String partnerCode;
}
