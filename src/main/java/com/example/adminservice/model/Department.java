package com.example.adminservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Department extends BaseModel {
    private String code;
    private String name;
    private String path;
    private String description;
    @Column(name = "parent_code")
    private String parentCode;
    @Column(name = "partner_code")
    private String partnerCode;
}
