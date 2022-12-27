package com.example.adminservice.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AppDTO {
    @NotBlank(message = "Mã không được để trống")
    @Size(min = 3, max = 20, message = "Mã phải có độ dài từ 3 -> 20")
    private String code;

    @NotNull(message = "Tên không được để trống")
    private String name;

    private String description;
}
