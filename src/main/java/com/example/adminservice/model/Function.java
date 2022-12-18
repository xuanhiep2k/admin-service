package com.example.adminservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Function extends BaseModel {
    @Column(name = "parent_code")
    private String parentCode;
    private String code;
    private String name;
    private String type;
    private String description;
    private String path;
    private String icon;
    @Column(name = "app_code")
    private String appCode;
}
