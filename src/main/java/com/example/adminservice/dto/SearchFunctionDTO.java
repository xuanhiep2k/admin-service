package com.example.adminservice.dto;

import com.example.adminservice.model.PageModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchFunctionDTO extends PageModel {
    private String code;
    private String name;
    private String type;
    private String appCode;
    private String parentCode;
    private String status;
}
