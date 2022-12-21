package com.example.adminservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class FunctionDTO {
    @NotBlank(message = "Mã không được để trống")
    @Size(min = 3, max = 20, message = "Mã phải có độ dài từ 3 -> 20")
    private String code;

    @NotNull(message = "Tên không được để trống")
    private String name;

    private String type;
    private String description;
    private String path;
    private String icon;
    @Column(name = "parent_code")
    private String parentCode;
    @Column(name = "app_code")
    private String appCode;
}
