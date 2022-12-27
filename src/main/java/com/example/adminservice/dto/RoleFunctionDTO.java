package com.example.adminservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleFunctionDTO {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String status;
    private String function;
    private Long functionid;
}
